package com.epam.net;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
@Log4j2
public class Server {
    private final int port;
    private final int bufferCapacity;
    private final Respondent respondent = new Respondent();

    public Server(int port, int bufferCapacity) {
        this.port = port;
        this.bufferCapacity = bufferCapacity;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = openAndBind(port);
             Selector selector = Selector.open()) {
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                selector.selectedKeys().removeIf(key -> {
                    if (!key.isValid()) {
                        log.debug("Key is not valid");
                    } else if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                    return true;
                });
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private ServerSocketChannel openAndBind(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        return serverSocketChannel;
    }

    private void accept(SelectionKey key) {
        try {
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            socketChannel.configureBlocking(false);
            SelectionKey newKey = socketChannel.register(key.selector(), SelectionKey.OP_READ);
            newKey.attach(ByteBuffer.allocateDirect(bufferCapacity));
            log.info(String.format("Accepted {%s}", socketChannel));
        } catch (IOException e) {
            log.error("Error while accepting");
        }
    }

    @SneakyThrows
    private void read(SelectionKey key) {
        val socketChannel = (SocketChannel) key.channel();
        val buffer = (ByteBuffer) key.attachment();
        //лайтовая версия - считаем, что считали сразу все нужные байты
        //TODO: проверять на получение всего сообщения (либо нужной части)
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead == -1 && buffer.position() == 0) return;
        log.debug(String.format("Received %d bytes", bytesRead));
        buffer.flip();
        key.interestOps(SelectionKey.OP_WRITE);
    }

    //TODO: рассмотреть ситуацию Connection: keep-alive (не закрывать канал каждый раз)
    @SneakyThrows
    private void write(SelectionKey key) {
        //channel closes here
        try (val socketChannel = (SocketChannel) key.channel()) {
            val buffer = (ByteBuffer) key.attachment();
            val requestBytes = new byte[buffer.limit()];
            buffer.get(requestBytes, 0, buffer.limit());
            val request = new String(requestBytes);
            log.debug(String.format("Request: %s", request));
            buffer.clear();

            String response = respondent.getResponse(request);
            log.debug(String.format("Response: %s", response));
            byte[] responseBytes = response.getBytes();
            for (int i = 0; i < responseBytes.length; i += bufferCapacity) {
                int limit = i + bufferCapacity;
                if (limit > responseBytes.length) limit = responseBytes.length;
                buffer.put(Arrays.copyOfRange(responseBytes, i, limit));
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
        }
    }

    public static void main(String[] args) {
        new Server(1024, 1024).start();
    }
}