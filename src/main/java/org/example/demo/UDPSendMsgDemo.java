package org.example.demo;

import org.example.communicator.CommunicationTask;
import org.example.communicator.UDPSendMsgTask;
import org.example.udp.UDPApp;
import org.example.udp.UDPThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UDPSendMsgDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(UDPSendMsgDemo.class);

    public static void main(String[] args) throws InterruptedException {
        final CommunicationTask communicationTask = new UDPSendMsgTask("localhost", 12345, 64, 10);
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
