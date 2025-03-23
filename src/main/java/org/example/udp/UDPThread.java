package org.example.udp;

import org.example.communicator.CommunicationTask;

public class UDPThread extends Thread {
    private final CommunicationTask communicationTask;

    public UDPThread(String name, CommunicationTask communicationTask) {
        super(name);
        this.communicationTask = communicationTask;
    }

    @Override
    public void run() {
        super.run();

        communicationTask.start();
    }

    @Override
    public void interrupt() {
        super.interrupt();

        communicationTask.stop();
    }
}
