package org.example.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPStringEchoTask implements CommunicationTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(UDPStringEchoTask.class);

    private final int port;
    private final byte[] receiveBuffer;

    private DatagramSocket socket;
    private volatile boolean running = false;

    public UDPStringEchoTask(int port, int bufferSize) {
        this.port = port;
        receiveBuffer = new byte[bufferSize];
    }

    @Override
    public void start() {
        try {
            socket = new DatagramSocket(port);
            LOGGER.info("listening on {} port {}", socket.getLocalAddress(), socket.getLocalPort());
        } catch (SocketException e) {
            LOGGER.error("cannot open socket", e);
        }
        if (socket == null) {
            return;
        }

        running = true;
        while (running) {
            final DatagramPacket datagramPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            try {
                // wait for next packet, blocking
                socket.receive(datagramPacket);
            } catch (SocketException e) {
                LOGGER.info("socket closed, cannot receive packet");
                running = false;
                break;
            } catch (IOException e) {
                LOGGER.error("receive packet error", e);
                continue;
            }

            final InetAddress remoteAddr = datagramPacket.getAddress();
            final int remotePort = datagramPacket.getPort();
            final String strMsg = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            LOGGER.info("received from {}:{}: {}", remoteAddr, remotePort, strMsg);

            if (strMsg.equals("end")) {
                LOGGER.info("end communication");
                running = false;
                break;
            }

            final DatagramPacket echoPacket = new DatagramPacket(datagramPacket.getData(), datagramPacket.getLength(), remoteAddr, remotePort);
            try {
                socket.send(echoPacket);
            } catch (SocketException e) {
                LOGGER.info("socket closed, cannot send packet");
                running = false;
                break;
            } catch (IOException e) {
                LOGGER.error("send packet error", e);
            }
        }

        LOGGER.info("finished task");
        socket.close();
        LOGGER.info("finished cleanup");
    }

    @Override
    public void stop() {
        running = false;
        if (socket != null) {
            if (socket.isClosed()) {
                LOGGER.info("socket is closed already");
            } else {
                LOGGER.info("closing socket");
                socket.close();
            }
        }
    }
}
