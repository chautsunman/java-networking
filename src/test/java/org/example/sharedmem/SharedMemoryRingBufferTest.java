package org.example.sharedmem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SharedMemoryRingBufferTest {
    private static final String FILE_PATH = "./shared_memory.bin";

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_PATH));
    }

    @Test
    public void testWriteThenRead() throws InterruptedException {
        final Thread writeThread = new Thread(() -> {
            try {
                final SharedMemoryRingBuffer sharedMemoryRingBuffer = new SharedMemoryRingBuffer(FILE_PATH, 1024);
                final String msg = "Hello, World!";
                for (int i = 0; i < msg.length(); i++) {
                    sharedMemoryRingBuffer.writeChar(msg.charAt(i));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        writeThread.start();
        writeThread.join();

        final Thread readThread = new Thread(() -> {
            try {
                final SharedMemoryRingBuffer sharedMemoryRingBuffer = new SharedMemoryRingBuffer(FILE_PATH, 1024);
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 13; i++) {
                    sb.append(sharedMemoryRingBuffer.readChar());
                }
                assertEquals("Hello, World!", sb.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        readThread.start();
        readThread.join();
    }

    @Test
    public void testConcurrentReadWrite() throws InterruptedException {
        final long testTimeout = System.currentTimeMillis() + 10000;
        final int msgCnt = 1000;

        final AtomicInteger fullCnt = new AtomicInteger();
        final AtomicInteger emptyCnt = new AtomicInteger();

        final Thread writeThread = new Thread(() -> {
            try {
                final SharedMemoryRingBuffer sharedMemoryRingBuffer = new SharedMemoryRingBuffer(FILE_PATH, 1024);
                final String msg = "Hello, World!";
                for (int times = 0; times < msgCnt; times++) {
                    for (int i = 0; i < msg.length(); i++) {
                        while (System.currentTimeMillis() < testTimeout) {
                            try {
                                sharedMemoryRingBuffer.writeChar(msg.charAt(i));
                                break;
                            } catch (IllegalStateException e) {
                                if (e.getMessage().equals("buffer is full")) {
                                    fullCnt.getAndIncrement();
                                } else {
                                    throw e;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        writeThread.start();

        final Thread readThread = new Thread(() -> {
            try {
                final SharedMemoryRingBuffer sharedMemoryRingBuffer = new SharedMemoryRingBuffer(FILE_PATH, 1024);
                final StringBuilder sb = new StringBuilder();
                for (int times = 0; times < msgCnt; times++) {
                    for (int i = 0; i < 13; i++) {
                        while (System.currentTimeMillis() < testTimeout) {
                            try {
                                sb.append(sharedMemoryRingBuffer.readChar());
                                break;
                            } catch (IllegalStateException e) {
                                if (e.getMessage().equals("no data to read")) {
                                    emptyCnt.getAndIncrement();
                                } else {
                                    throw e;
                                }
                            }
                        }
                    }
                }
                assertEquals("Hello, World!".repeat(msgCnt), sb.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        readThread.start();

        writeThread.join();
        readThread.join();

        System.out.println(String.format("fullCnt: %d, emptyCnt: %d", fullCnt.get(), emptyCnt.get()));
    }
}
