package org.example.demo;

import org.example.communicator.CommunicationTask;
import org.example.communicator.UDPStringEchoTask;
import org.example.udp.UDPApp;
import org.example.udp.UDPThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UDPEchoDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(UDPEchoDemo.class);

    public static void main(String[] args) throws InterruptedException {
        final CommunicationTask communicationTask = new UDPStringEchoTask(12345, 64);
        final UDPThread udpThread = new UDPThread("echo", communicationTask);
        final UDPApp udpApp = new UDPApp(udpThread);
        udpApp.start();
        LOGGER.info("started udpApp");
        Thread.sleep(10000);
        LOGGER.info("ending udpApp");
        udpApp.stop();
        LOGGER.info("ended udpApp");
    }
}
