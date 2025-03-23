package org.example.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class UDPSendMsgTask implements CommunicationTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(UDPSendMsgTask.class);

    private final InetAddress inetAddress;
    private final int port;
    private final int maxMsgCnt;

    private DatagramSocket socket;
    private volatile boolean running = false;
    private int msgCnt = 0;
    private byte[] sendBuffer;

    public UDPSendMsgTask(String ip, int port, int bufferSize, int maxMsgCnt) {
        try {
            inetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.port = port;
        this.maxMsgCnt = maxMsgCnt;
    }

    @Override
    public void start() {
        try {
            socket = new DatagramSocket();
            LOGGER.info("created socket, local addr: {}, local port: {}", socket.getLocalAddress(), socket.getLocalPort());
        } catch (SocketException e) {
            LOGGER.error("cannot open socket", e);
        }
        if (socket == null) {
            return;
        }

        running = true;
        while (running && msgCnt < maxMsgCnt) {
            sendBuffer = ("msg-" + msgCnt + "-" + System.nanoTime()).getBytes();
            final DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetAddress, port);
            try {
                socket.send(datagramPacket);
                LOGGER.info("sent: {}", new String(sendBuffer));
            } catch (SocketException e) {
                LOGGER.info("socket closed, cannot send packet");
                running = false;
                break;
            } catch (IOException e) {
                LOGGER.error("send packet error", e);
            }
            msgCnt++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // re-interrupt the current thread
                Thread.currentThread().interrupt();
                LOGGER.info("interrupted sleep, msgCnt: {}", msgCnt);
                running = false;
                break;
            }
        }

        sendBuffer = "end".getBytes();
        final DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetAddress, port);
        try {
            socket.send(datagramPacket);
            LOGGER.info("sent end");
        } catch (SocketException e) {
            LOGGER.info("socket closed, cannot send end");
        } catch (IOException e) {
            LOGGER.error("send end error", e);
        }

        LOGGER.info("finished task, msgCnt: {}", msgCnt);
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
