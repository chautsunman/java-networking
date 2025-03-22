package org.example.demo;

import org.example.communicator.BasicCommunicator;
import org.example.tcp.TCPClient;

public class MultipleTCPClientsDemo {
    public static void main(String[] args) throws InterruptedException {
        final TCPClient tcpClient = new TCPClient("localhost", 12345, new BasicCommunicator("client1"));
        final TCPClient tcpClient2 = new TCPClient("localhost", 12345, new BasicCommunicator("client2"));
        tcpClient.start();
        tcpClient2.start();
        Thread.sleep(10000);
        tcpClient.stop();
        tcpClient2.stop();
    }
}
