package org.example.tcp;

import org.example.communicator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServerProcessingThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(TCPServerProcessingThread.class);

    private final Socket socket;
    private final Communicator communicator;

    private BufferedReader input;
    private PrintWriter output;

    public TCPServerProcessingThread(String name, Socket socket, Communicator communicator) {
        super(name);
        this.socket = socket;
        this.communicator = communicator;
    }

    @Override
    public void run() {
        super.run();

        try {
            // create input and output streams
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            LOGGER.info("created input and output streams");

            communicator.process(input, output);
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();

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
            LOGGER.error("error", e);
        }
    }
}
