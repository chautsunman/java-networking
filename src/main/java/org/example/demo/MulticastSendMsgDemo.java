package org.example.demo;

import org.example.communicator.CommunicationTask;
import org.example.communicator.UDPSendMsgTask;
import org.example.udp.UDPApp;
import org.example.udp.UDPThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulticastSendMsgDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastSendMsgDemo.class);

    public static void main(String[] args) throws InterruptedException {
        // IPv4 multicast range = 224.0.0.0 to 239.255.255.255
        final CommunicationTask communicationTask = new UDPSendMsgTask("224.0.0.0", 12345, 64, 10);
        final UDPThread udpThread = new UDPThread("msg", communicationTask);
        final UDPApp udpApp = new UDPApp(udpThread);
        udpApp.start();
        LOGGER.info("started udpApp");
        Thread.sleep(10000);
        LOGGER.info("ending udpApp");
        udpApp.stop();
        LOGGER.info("ended udpApp");
    }
}
