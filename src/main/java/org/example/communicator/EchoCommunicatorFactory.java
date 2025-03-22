package org.example.communicator;

public class EchoCommunicatorFactory implements CommunicatorFactory {
    @Override
    public Communicator create(String name) {
        return new EchoCommunicator(name);
    }
}
