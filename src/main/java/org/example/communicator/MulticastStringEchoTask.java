package org.example.communicator;

import java.io.IOException;
import java.net.*;

public class MulticastStringEchoTask extends AbstractUDPStringEchoTask<MulticastSocket> {
    private final InetAddress multicastAddr;

    public MulticastStringEchoTask(int port, int bufferSize, String multicastIp) throws UnknownHostException {
        super(port, bufferSize);
        multicastAddr = InetAddress.getByName(multicastIp);
    }

    @Override
    protected MulticastSocket createSocket() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(port);
            socket.joinGroup(multicastAddr);
            logger.info("listening on {} port {}", multicastAddr, socket.getLocalPort());
        } catch (SocketException e) {
            logger.error("cannot open socket", e);
        } catch (IOException e) {
            logger.error("cannot open socket", e);
        }
        return socket;
    }

    @Override
    protected void closeSocket() {
        if (socket != null) {
            if (socket.isClosed()) {
                logger.info("socket is closed already");
            } else {
                logger.info("closing socket");
                try {
                    socket.leaveGroup(multicastAddr);
                } catch (IOException e) {
                    logger.error("cannot leave group {}", multicastAddr, e);
                }
                socket.close();
            }
        }
    }
}
