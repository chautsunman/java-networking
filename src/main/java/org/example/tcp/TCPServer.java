package org.example.tcp;

import org.example.base.BasicLifecycle;
import org.example.communicator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class TCPServer implements BasicLifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(TCPServer.class);

    private final int port;
    private final Communicator communicator;

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public TCPServer(int port, Communicator communicator) {
        this.port = port;
        this.communicator = communicator;
    }

    @Override
    public void start() {
        try {
            // create socket
            serverSocket = new ServerSocket(port);
            LOGGER.info("server is listening on {} port {}", serverSocket.getInetAddress(), serverSocket.getLocalPort());

            // wait for client connection
            // blocking until a connection is being established
            socket = serverSocket.accept();
            LOGGER.info("a client has connected, remote IP: {}", socket.getInetAddress());

            // create input and output streams
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            LOGGER.info("created input and output streams");

            communicator.process(input, output);
        } catch (IOException e) {
            LOGGER.error("server error", e);
        }
    }

    @Override
    public void stop() {
        try {
            if (communicator != null) {
                communicator.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.error("stop error", e);
        }
    }
}
