package org.example.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BasicCommunicator implements Communicator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicCommunicator.class);

    private final String name;

    private Thread listenMsgThread;
    private volatile boolean listenMsgRunning = false;

    private Thread basicCommunicatorThread;

    public BasicCommunicator(String name) {
        this.name = name;
    }

    @Override
    public void process(BufferedReader input, PrintWriter output) {
        listenMsgThread = new Thread(() -> {
            while (listenMsgRunning) {
                try {
                    final String message = input.readLine();
                    if (message == null) {
                        LOGGER.info("stream ended");
                        break;
                    }
                    LOGGER.info("received: {}", message);
                } catch (IOException e) {
                    LOGGER.error("error", e);
                }
            }

            LOGGER.info("stopped listening");
        }, name + "-listen");

        basicCommunicatorThread = new Thread(() -> {
            output.println("hello");
            LOGGER.info("sent hello");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // re-interrupt the current thread
                Thread.currentThread().interrupt();
                LOGGER.info("interrupted sleep 1");
            }

            output.println("hello2");
            LOGGER.info("sent hello2");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // re-interrupt the current thread
                Thread.currentThread().interrupt();
                LOGGER.info("interrupted sleep 2");
            }

            output.println("end");
            LOGGER.info("sent end");
        }, name + "-communicator");

        listenMsgRunning = true;
        listenMsgThread.start();
        basicCommunicatorThread.start();
    }

    @Override
    public void close() throws IOException {
        if (basicCommunicatorThread != null) {
            basicCommunicatorThread.interrupt();
        }
        listenMsgRunning = false;
        if (listenMsgThread != null) {
            listenMsgThread.interrupt();
        }
    }
}
