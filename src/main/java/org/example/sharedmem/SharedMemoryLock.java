package org.example.sharedmem;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class SharedMemoryLock {
    public static final long DEFAULT_LOCK_TIMEOUT_MS = 100;

    private final Unsafe unsafe;

    // memory address of the lock
    // lock is represented by int
    // 0: unlocked
    // 1: locked
    private final long address;

    public SharedMemoryLock(long address) throws Exception {
        unsafe = getUnsafe();
        this.address = address;
    }

    public boolean tryLock(long timeout) {
        final long timeoutTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < timeoutTime) {
            // compare and swap to lock
            // null object + addr --> addr represents raw memory address
            if (unsafe.compareAndSwapInt(null, address, 0, 1)) {
                return true;
            }
        }
        return false;
    }

    public void unlock() {
        // unlock by setting the lock state to 0 directly
        unsafe.putInt(address, 0);
    }

    private Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }
}
