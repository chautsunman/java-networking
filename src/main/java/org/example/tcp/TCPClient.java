package org.example.tcp;

import org.example.base.BasicLifecycle;
import org.example.communicator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class TCPClient implements BasicLifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(TCPClient.class);

    private final String host;
    private final int port;
    private final Communicator communicator;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public TCPClient(String host, int port, Communicator communicator) {
        this.host = host;
        this.port = port;
        this.communicator = communicator;
    }

    @Override
    public void start() {
        try {
            // connect to server
            socket = new Socket(host, port);
            LOGGER.info("connected to server, host: {}", socket.getInetAddress());

            // create input and output streams
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            LOGGER.info("created input and output streams");

            communicator.process(input, output);
        } catch (IOException e) {
            LOGGER.error("client error", e);
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
        } catch (IOException e) {
            LOGGER.error("stop error", e);
        }
    }
}
