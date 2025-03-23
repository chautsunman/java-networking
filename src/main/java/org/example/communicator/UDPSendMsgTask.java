package org.example.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class UDPSendMsgTask implements CommunicationTask {
    private final InetAddress inetAddress;
    private final int port;
    private final int maxMsgCnt;
    protected final Logger logger;

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
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void start() {
        socket = createSocket();
        if (socket == null) {
            return;
        }

        running = true;
        while (running && msgCnt < maxMsgCnt) {
            sendBuffer = ("msg-" + msgCnt + "-" + System.nanoTime()).getBytes();
            final DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetAddress, port);
            try {
                socket.send(datagramPacket);
                logger.info("sent: {}", new String(sendBuffer));
            } catch (SocketException e) {
                logger.info("socket closed, cannot send packet");
                running = false;
                break;
            } catch (IOException e) {
                logger.error("send packet error", e);
            }
            msgCnt++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // re-interrupt the current thread
                Thread.currentThread().interrupt();
                logger.info("interrupted sleep, msgCnt: {}", msgCnt);
                running = false;
                break;
            }
        }

        sendBuffer = "end".getBytes();
        final DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, inetAddress, port);
        try {
            socket.send(datagramPacket);
            logger.info("sent end");
        } catch (SocketException e) {
            logger.info("socket closed, cannot send end");
        } catch (IOException e) {
            logger.error("send end error", e);
        }

        logger.info("finished task, msgCnt: {}", msgCnt);
        cleanUp();
        logger.info("finished cleanup");
    }

    protected DatagramSocket createSocket() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            logger.info("created socket, local: {}:{}, remote: {}:{}", socket.getLocalAddress(), socket.getLocalPort(), socket.getInetAddress(), socket.getPort());
        } catch (SocketException e) {
            logger.error("cannot open socket", e);
        }
        return socket;
    }

    @Override
    public void stop() {
        running = false;
        cleanUp();
    }

    protected void cleanUp() {
        closeSocket();
    }

    protected void closeSocket() {
        if (socket != null) {
            if (socket.isClosed()) {
                logger.info("socket is closed already");
            } else {
                logger.info("closing socket");
                socket.close();
            }
        }
    }
}
