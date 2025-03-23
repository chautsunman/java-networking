package org.example.communicator;

import java.net.DatagramSocket;
import java.net.SocketException;

public class BroadcastSendMsgTask extends UDPSendMsgTask {
    public BroadcastSendMsgTask(String ip, int port, int bufferSize, int maxMsgCnt) {
        super(ip, port, bufferSize, maxMsgCnt);
    }

    @Override
    protected DatagramSocket createSocket() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            logger.info("created socket, local: {}:{}, remote: {}:{}", socket.getLocalAddress(), socket.getLocalPort(), socket.getInetAddress(), socket.getPort());
        } catch (SocketException e) {
            logger.error("cannot open socket", e);
        }
        return socket;
    }
}
