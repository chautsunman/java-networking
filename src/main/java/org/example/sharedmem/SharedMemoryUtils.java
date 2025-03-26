package org.example.sharedmem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SharedMemoryUtils {
    public static MappedByteBuffer createSharedMemory(String filePath, String mode, int sizeBytes) {
        try {
            final RandomAccessFile file = new RandomAccessFile(filePath, mode);
            final FileChannel channel = file.getChannel();
            return channel.map(FileChannel.MapMode.READ_WRITE, 0, sizeBytes);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static long getSharedMemoryAddress(MappedByteBuffer sharedMemory) {
        try {
            // shared memory should be an off-heap direct buffer
            return ((sun.nio.ch.DirectBuffer) sharedMemory).address();
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
