package org.example.demo;

import org.example.communicator.Communicator;
import org.example.communicator.EchoCommunicator;
import org.example.tcp.TCPServer;

public class TCPServerDemo {
    public static void main(String[] args) throws InterruptedException {
        final Communicator communicator = new EchoCommunicator();
        final TCPServer tcpServer = new TCPServer(12345, communicator);
        tcpServer.start();
        Thread.sleep(10000);
        tcpServer.stop();
    }
}
