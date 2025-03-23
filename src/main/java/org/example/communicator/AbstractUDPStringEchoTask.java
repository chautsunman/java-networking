package org.example.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class AbstractUDPStringEchoTask<S extends DatagramSocket> implements CommunicationTask {
    protected final int port;
    private final byte[] receiveBuffer;
    protected final Logger logger;

    protected S socket;
    private volatile boolean running = false;

    public AbstractUDPStringEchoTask(int port, int bufferSize) {
        this.port = port;
        receiveBuffer = new byte[bufferSize];
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void start() {
        socket = createSocket();
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
                logger.info("socket closed, cannot receive packet");
                running = false;
                break;
            } catch (IOException e) {
                logger.error("receive packet error", e);
                continue;
            }

            final InetAddress remoteAddr = datagramPacket.getAddress();
            final int remotePort = datagramPacket.getPort();
            final String strMsg = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            logger.info("received from {}:{}: {}", remoteAddr, remotePort, strMsg);

            if (strMsg.equals("end")) {
                logger.info("end communication");
                running = false;
                break;
            }

            final DatagramPacket echoPacket = new DatagramPacket(datagramPacket.getData(), datagramPacket.getLength(), remoteAddr, remotePort);
            try {
                socket.send(echoPacket);
            } catch (SocketException e) {
                logger.info("socket closed, cannot send packet");
                running = false;
                break;
            } catch (IOException e) {
                logger.error("send packet error", e);
            }
        }

        logger.info("finished task");
        cleanUp();
        logger.info("finished cleanup");
    }

    protected abstract S createSocket();

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
