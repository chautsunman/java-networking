package org.example.sharedmem;

import java.nio.MappedByteBuffer;

public class SharedMemoryRingBuffer {
    // active file indicator
    private static final int HEADER_POS_ACTIVE_FILE = 0;
    // next byte index to read from
    private static final int HEADER_POS_READ_IDX = 1 * Integer.BYTES;
    // next byte index to write to
    private static final int HEADER_POS_WRITE_IDX = 2 * Integer.BYTES;
    // shared memory lock
    private static final int HEADER_POS_LOCK = 3 * Integer.BYTES;
    // 64-byte header
    private static final int HEADER_SIZE_BYTES = 64;

    private final int dataStartIdx;
    private final int dataSize;
    private final MappedByteBuffer sharedMemory;
    private final SharedMemoryLock sharedMemoryLock;

    public SharedMemoryRingBuffer(String filePath, int sizeBytes) throws Exception {
        if ((sizeBytes - HEADER_SIZE_BYTES) % Character.BYTES != 0) {
            throw new IllegalArgumentException("sizeBytes must be a multiple of " + Character.BYTES);
        }

        this.dataStartIdx = HEADER_SIZE_BYTES;
        this.dataSize = sizeBytes - HEADER_SIZE_BYTES;
        sharedMemory = SharedMemoryUtils.createSharedMemory(filePath, "rw", sizeBytes);
        if (sharedMemory == null) {
            throw new RuntimeException("cannot create shared memory");
        }
        final long sharedMemoryAddress = SharedMemoryUtils.getSharedMemoryAddress(sharedMemory);
        if (sharedMemoryAddress == Long.MIN_VALUE) {
            throw new RuntimeException("cannot get shared memory address");
        }
        final long sharedMemoryLockAddress = sharedMemoryAddress + HEADER_POS_LOCK;
        sharedMemoryLock = new SharedMemoryLock(sharedMemoryLockAddress);

        init();
    }

    private void init() {
        // lock the shared memory for initialization
        if (!sharedMemoryLock.tryLock(SharedMemoryLock.DEFAULT_LOCK_TIMEOUT_MS)) {
            throw new RuntimeException("cannot acquire lock");
        }
        try {
            if (!isActiveFile()) {
                // initialize the header
                setReadIdx(dataStartIdx);
                setWriteIdx(dataStartIdx);
                // mark the file as active
                markFileActive();
            }
        } finally {
            sharedMemoryLock.unlock();
        }
    }

    public void writeChar(char value) {
        if (!sharedMemoryLock.tryLock(SharedMemoryLock.DEFAULT_LOCK_TIMEOUT_MS)) {
            throw new RuntimeException("cannot acquire lock");
        }
        try {
            if (isFull()) {
                throw new IllegalStateException("buffer is full");
            }
            int writeIdx = getWriteIdx();
            sharedMemory.putChar(writeIdx, value);
            writeIdx = getNextIdx(writeIdx);
            setWriteIdx(writeIdx);
        } finally {
            sharedMemoryLock.unlock();
        }
    }

    public char readChar() {
        if (!sharedMemoryLock.tryLock(SharedMemoryLock.DEFAULT_LOCK_TIMEOUT_MS)) {
            throw new RuntimeException("cannot acquire lock");
        }
        try {
            if (isEmpty()) {
                throw new IllegalStateException("no data to read");
            }
            int readIdx = getReadIdx();
            final char value = sharedMemory.getChar(readIdx);
            readIdx = getNextIdx(readIdx);
            setReadIdx(readIdx);
            return value;
        } finally {
            sharedMemoryLock.unlock();
        }
    }

    private boolean isEmpty() {
        return getReadIdx() == getWriteIdx();
    }

    private boolean isFull() {
        return getNextIdx(getWriteIdx()) == getReadIdx();
    }

    private int getNextIdx(int idx) {
        int nextIdx = idx + Character.BYTES;
        if (nextIdx >= dataSize) {
            nextIdx = dataStartIdx;
        }
        return nextIdx;
    }

    private boolean isActiveFile() {
        return sharedMemory.getInt(HEADER_POS_ACTIVE_FILE) == 1;
    }

    private void markFileActive() {
        sharedMemory.putInt(HEADER_POS_ACTIVE_FILE, 1);
    }

    private int getReadIdx() {
        return sharedMemory.getInt(HEADER_POS_READ_IDX);
    }

    private void setReadIdx(int readIdx) {
        sharedMemory.putInt(HEADER_POS_READ_IDX, readIdx);
    }

    private int getWriteIdx() {
        return sharedMemory.getInt(HEADER_POS_WRITE_IDX);
    }

    private void setWriteIdx(int writeIdx) {
        sharedMemory.putInt(HEADER_POS_WRITE_IDX, writeIdx);
    }
}
