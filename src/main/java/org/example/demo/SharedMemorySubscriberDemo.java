package org.example.demo;

import org.example.sharedmem.SharedMemoryRingBuffer;

public class SharedMemorySubscriberDemo {
    private static final String FILE_PATH = "./shared_memory.bin";

    public static void main(String[] args) throws Exception {
        final SharedMemoryRingBuffer sharedMemoryRingBuffer = new SharedMemoryRingBuffer(FILE_PATH, 1024);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(sharedMemoryRingBuffer.readChar());
        }
        System.out.println(sb.toString());
    }
}
