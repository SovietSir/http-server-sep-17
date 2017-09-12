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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("WeakerAccess")
@Log4j2
public class Server {
    private final int port;
    private final int bufferCapacity;
    private final Respondent respondent = new Respondent();
    private final NoDuplicatingBlockingQueue queue = new NoDuplicatingBlockingQueue();

    public Server(int port, int bufferCapacity) {
        this.port = port;
        this.bufferCapacity = bufferCapacity;
    }

    @SneakyThrows
    public void start() {
        log.info("Server loading...");
        startQueueExecutors();
        try (ServerSocketChannel serverSocketChannel = openAndBindChannel(port);
             Selector selector = Selector.open()) {
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.info(() -> String.format("Server started, please visit: http://localhost:%s%n", port));
            while (!Thread.currentThread().isInterrupted()) {
                selector.selectNow();
                Set<SelectionKey> keys = selector.selectedKeys();
                keys.stream()
                        .filter(SelectionKey::isValid)
                        .filter(queue::notContains)
                        .forEach(queue::add);
                keys.clear();
            }
        }
    }

    private void startQueueExecutors() {
        Executors.newSingleThreadExecutor().execute(() -> {
            ExecutorService pool = Executors.newCachedThreadPool();
            while (!Thread.currentThread().isInterrupted()) {
                SelectionKey key = queue.getKeyToExecute();
                pool.execute(() -> execute(key));
            }
        });
    }

    private void execute(SelectionKey key) {
        try {
            if (key.isAcceptable()) accept(key);
            else if (key.isReadable()) read(key);
            else if (key.isWritable()) write(key);
        } finally {
            queue.removeExecutedKey(key);
        }
    }

    private ServerSocketChannel openAndBindChannel(int port) throws IOException {
        return (ServerSocketChannel) ServerSocketChannel.open()
                .bind(new InetSocketAddress(port))
                .configureBlocking(false);
    }

    @SneakyThrows
    private void accept(SelectionKey key) {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        SelectionKey newKey = socketChannel.register(key.selector(), SelectionKey.OP_READ);
        newKey.attach(ByteBuffer.allocateDirect(bufferCapacity));
        log.info(() -> String.format("Accepted {%s}", socketChannel));
    }

    @SneakyThrows
    private void read(SelectionKey key) {
        val socketChannel = (SocketChannel) key.channel();
        val buffer = (ByteBuffer) key.attachment();
        int sumBytesRead = 0;
        for (int bytesRead = 1; bytesRead > 0 && buffer.hasRemaining(); ) {
            bytesRead = socketChannel.read(buffer);
            sumBytesRead += bytesRead;
        }
        logReadBytes(sumBytesRead);
        buffer.flip();
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void logReadBytes(int bytes) {
        log.debug(() -> String.format("Received %d bytes", bytes));
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
