package org.example.demo;

import org.example.communicator.CommunicatorFactory;
import org.example.communicator.EchoCommunicatorFactory;
import org.example.tcp.MultiClientTCPServer;

public class MultiClientTCPServerDemo {
    public static void main(String[] args) throws InterruptedException {
        final CommunicatorFactory communicatorFactory = new EchoCommunicatorFactory();
        final MultiClientTCPServer tcpServer = new MultiClientTCPServer(12345, communicatorFactory);
        tcpServer.start();
        Thread.sleep(10000);
        tcpServer.stop();
    }
}
