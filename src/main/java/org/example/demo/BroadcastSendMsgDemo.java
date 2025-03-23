package org.example.demo;

import org.example.communicator.BroadcastSendMsgTask;
import org.example.communicator.CommunicationTask;
import org.example.udp.UDPApp;
import org.example.udp.UDPThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcastSendMsgDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastSendMsgDemo.class);

    public static void main(String[] args) throws InterruptedException {
        // localhost broadcast address = 255.255.255.255
        final CommunicationTask communicationTask = new BroadcastSendMsgTask("255.255.255.255", 12345, 64, 10);
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
