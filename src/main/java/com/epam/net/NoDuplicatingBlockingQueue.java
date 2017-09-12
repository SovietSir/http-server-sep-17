package com.epam.net;

import lombok.SneakyThrows;

import java.nio.channels.SelectionKey;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

// TODO проверить на корректную работу в случае большого числа потоков
// в частности, не будет ли ошибок из за отсутствия принудительной синхронизизации большей части методов
class NoDuplicatingBlockingQueue {
    private final ArrayBlockingQueue<SelectionKey> socketQueue = new ArrayBlockingQueue<>(100);
    private final Set<SelectionKey> keysInWork = Collections.synchronizedSet(new HashSet<>());

    void add(SelectionKey key) {
        if (!socketQueue.contains(key) && !keysInWork.contains(key))
            socketQueue.add(key);
    }

    SelectionKey getKeyToExecute() {
        synchronized (socketQueue) {
            SelectionKey key = getNextKey();
            keysInWork.add(key);
            return key;
        }
    }

    @SneakyThrows
    private SelectionKey getNextKey() {
        return socketQueue.take();
    }

    boolean notContains(SelectionKey key) {
        return !contains(key);
    }

    private boolean contains(SelectionKey key) {
        return socketQueue.contains(key) || keysInWork.contains(key);
    }

    void removeExecutedKey(SelectionKey key) {
        keysInWork.remove(key);
    }

}
