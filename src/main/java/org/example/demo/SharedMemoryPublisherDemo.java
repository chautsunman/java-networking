package org.example.demo;

import org.example.sharedmem.SharedMemoryRingBuffer;

public class SharedMemoryPublisherDemo {
    private static final String FILE_PATH = "./shared_memory.bin";

    public static void main(String[] args) throws Exception {
        final SharedMemoryRingBuffer sharedMemoryRingBuffer = new SharedMemoryRingBuffer(FILE_PATH, 1024);
        final String msg = "Hello, World!";
        for (int i = 0; i < msg.length(); i++) {
            sharedMemoryRingBuffer.writeChar(msg.charAt(i));
        }

/*
hexdump -C shared_memory.bin
00000000  00 00 00 01 00 00 00 40  00 00 00 5a 00 00 00 00  |.......@...Z....|
00000010  00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00  |................|
*
00000040  00 48 00 65 00 6c 00 6c  00 6f 00 2c 00 20 00 57  |.H.e.l.l.o.,. .W|
00000050  00 6f 00 72 00 6c 00 64  00 21 00 00 00 00 00 00  |.o.r.l.d.!......|
00000060  00 00 00 00 00 00 00 00  00 00 00 00 00 00 00 00  |................|
*
00000400

 */
    }
}
