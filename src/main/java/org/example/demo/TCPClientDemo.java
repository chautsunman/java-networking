package org.example.demo;

import org.example.communicator.BasicCommunicator;
import org.example.communicator.Communicator;
import org.example.tcp.TCPClient;

public class TCPClientDemo {
    public static void main(String[] args) throws InterruptedException {
        final Communicator communicator = new BasicCommunicator();
        final TCPClient tcpClient = new TCPClient("localhost", 12345, communicator);
        tcpClient.start();
        Thread.sleep(10000);
        tcpClient.stop();
    }
}
