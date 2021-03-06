package com.epam.net;

import com.epam.store.ConnectionPool;
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
import java.util.Set;

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

    @SneakyThrows
    public void start() {
        log.info(() -> String.format("Server started, please visit: http://localhost:%s%n", port));
        try (ServerSocketChannel serverSocketChannel = openAndBindChannel(port);
             Selector selector = Selector.open()) {
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                keys.stream()
                        .filter(SelectionKey::isValid)
                        .forEach(this::execute);
                keys.clear();
            }
        }
    }

    private void execute(SelectionKey key) {
        if (key.isAcceptable()) accept(key);
        else if (key.isReadable()) read(key);
        else if (key.isWritable()) write(key);
    }

    private ServerSocketChannel openAndBindChannel(int port) throws IOException {
        return (ServerSocketChannel) ServerSocketChannel.open()
                .bind(new InetSocketAddress(port))
                .configureBlocking(false);
    }

    private void accept(SelectionKey key) {
        try {
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            socketChannel.configureBlocking(false);
            SelectionKey newKey = socketChannel.register(key.selector(), SelectionKey.OP_READ);
            newKey.attach(ByteBuffer.allocateDirect(bufferCapacity));
            log.info(() -> String.format("Accepted {%s}", socketChannel));
        } catch (IOException e) {
            log.error("Error while accepting", e);
        }
    }

    @SneakyThrows
    private void read(SelectionKey key) {
        val socketChannel = (SocketChannel) key.channel();
        val buffer = (ByteBuffer) key.attachment();
        //лайтовая версия - считаем, что считали сразу все нужные байты
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead == -1 && buffer.position() == 0) return;
        log.debug(() -> String.format("Received %d bytes", bytesRead));
        buffer.flip();
        key.interestOps(SelectionKey.OP_WRITE);
    }

    @SneakyThrows
    private void write(SelectionKey key) {
        //channel closes here
        try (val socketChannel = (SocketChannel) key.channel()) {
            val buffer = (ByteBuffer) key.attachment();
            val requestBytes = new byte[buffer.limit()];
            buffer.get(requestBytes, 0, buffer.limit());
            val request = new String(requestBytes);
            log.debug(() -> String.format("Request:%n%s", request));
            buffer.clear();

            String response = respondent.getResponse(request).toString();
            log.debug(() -> String.format("Response:%n%s", response));
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
        ConnectionPool.pool.dropDatabase();
        ConnectionPool.pool.initDatabase();
        new Server(1024, 1024).start();
    }
}
