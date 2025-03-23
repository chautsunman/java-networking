package org.example.communicator;

import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPStringEchoTask extends AbstractUDPStringEchoTask<DatagramSocket> {
    public UDPStringEchoTask(int port, int bufferSize) {
        super(port, bufferSize);
    }

    @Override
    protected DatagramSocket createSocket() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            logger.info("listening on {} port {}", socket.getLocalAddress(), socket.getLocalPort());
        } catch (SocketException e) {
            logger.error("cannot open socket", e);
        }
        return socket;
    }
}
