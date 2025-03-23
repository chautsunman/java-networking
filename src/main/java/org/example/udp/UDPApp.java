package org.example.udp;

import org.example.base.BasicLifecycle;

public class UDPApp implements BasicLifecycle {
    private final UDPThread udpThread;

    public UDPApp(UDPThread udpThread) {
        this.udpThread = udpThread;
    }

    @Override
    public void start() {
        udpThread.start();
    }

    @Override
    public void stop() {
        if (udpThread != null) {
            udpThread.interrupt();
        }
    }
}
