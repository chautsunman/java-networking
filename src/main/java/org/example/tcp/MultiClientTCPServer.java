package org.example.tcp;

import org.example.base.BasicLifecycle;
import org.example.communicator.CommunicatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class MultiClientTCPServer implements BasicLifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiClientTCPServer.class);

    private final int port;
    private final CommunicatorFactory communicatorFactory;

    private ServerSocket serverSocket;
    private Thread serverThread;
    private volatile boolean running = false;
    private final List<Thread> threads;

    public MultiClientTCPServer(int port, CommunicatorFactory communicatorFactory) {
        this.port = port;
        this.communicatorFactory = communicatorFactory;

        threads = new ArrayList<>();
    }

    @Override
    public void start() {
        try {
            // create socket
            serverSocket = new ServerSocket(port);
            LOGGER.info("server is listening on {} port {}", serverSocket.getInetAddress(), serverSocket.getLocalPort());

            serverThread = new Thread(() -> {
                while (running) {
                    // wait for client connection
                    // blocking until a connection is being established
                    try {
                        final Socket socket = serverSocket.accept();
                        LOGGER.info("a client has connected, remote IP: {}, remote port: {}", socket.getInetAddress(), socket.getPort());

                        final String name = "client-" + socket.getInetAddress() + "-" + socket.getPort();
                        final Thread thread = new TCPServerProcessingThread(name, socket, communicatorFactory.create(name));
                        threads.add(thread);
                        thread.start();
                    } catch (SocketException e) {
                        LOGGER.info("server stopped");
                    } catch (IOException e) {
                        LOGGER.error("server error", e);
                    }
                }
            }, "server");
            running = true;
            serverThread.start();
        } catch (IOException e) {
            LOGGER.error("server error", e);
        }
    }

    @Override
    public void stop() {
        // stop server thread
        running = false;
        serverThread.interrupt();
        // stop all processing threads
        for (Thread thread : threads) {
            thread.interrupt();
        }
        try {
            // close server socket
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.error("stop error", e);
        }
    }
}
