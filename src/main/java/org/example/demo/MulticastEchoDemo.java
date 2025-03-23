package org.example.demo;

import org.example.communicator.CommunicationTask;
import org.example.communicator.MulticastStringEchoTask;
import org.example.udp.UDPApp;
import org.example.udp.UDPThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

public class MulticastEchoDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastEchoDemo.class);

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        // IPv4 multicast range = 224.0.0.0 to 239.255.255.255
        final CommunicationTask communicationTask = new MulticastStringEchoTask(12345, 64, "224.0.0.0");
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
