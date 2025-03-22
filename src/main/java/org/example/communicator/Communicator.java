package org.example.communicator;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.PrintWriter;

public interface Communicator extends Closeable {
    void process(BufferedReader input, PrintWriter output);
}
