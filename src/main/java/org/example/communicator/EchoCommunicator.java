package org.example.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class EchoCommunicator implements Communicator {
    private static final Logger LOGGER = LoggerFactory.getLogger(EchoCommunicator.class);

    private final String name;

    private Thread processThread;
    private volatile boolean running = false;

    public EchoCommunicator(String name) {
        this.name = name;
    }

    @Override
    public void process(BufferedReader input, PrintWriter output) {
        processThread = new Thread(() -> {
            while (running) {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        LOGGER.info("received: {}", message);
                        if (message.equals("end")) {
                            output.println("end communication");
                            running = false;
                            break;
                        }

                        output.println("echo: " + message);
                    }
                } catch (IOException e) {
                    LOGGER.error("error", e);
                }
            }

            LOGGER.info("stopped communication");
        }, name);

        running = true;
        processThread.start();
    }

    @Override
    public void close() throws IOException {
        running = false;
        if (processThread != null) {
            processThread.interrupt();
        }
    }
}
