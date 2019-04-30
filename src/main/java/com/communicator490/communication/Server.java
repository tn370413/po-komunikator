package com.communicator490.communication;

import java.net.SocketException;

public class Server {

    private int internalPort = 490;
    private ServerThread serverThread;

    public void stop() {
        serverThread.stopServerThread();

    }

    public int getInternalPort() {
        return internalPort;
    }

    private void start() throws SocketException {
        serverThread = new ServerThread(this.internalPort);
        this.internalPort = serverThread.getInternalPort();
        serverThread.start();
    }

    public Server() throws SocketException { // TODO add method to kill server on closing the app
        start();
    }
}
