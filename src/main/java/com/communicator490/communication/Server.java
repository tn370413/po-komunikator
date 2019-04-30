package com.communicator490.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    private DatagramSocket socket;
    private int internalPort = 490;
    private ServerThread serverThread;

    public void stop() {
        socket.close();
        serverThread.interrupt();
    }

    public int getInternalPort() {
        return internalPort;
    }

    private void start() throws SocketException {
        boolean success = false;
        while (!success && this.internalPort < 65536) {
            try {
                socket = new DatagramSocket(this.internalPort);
                success = true;
            } catch (SocketException e) {
                this.internalPort++;
            }
        }
        if (!success) {
            throw new SocketException("Can't establish server: no free port?");
        }
        serverThread = new ServerThread(socket, String.format("ServerOn%dThread", internalPort));
        serverThread.start();
    }

    public Server() throws SocketException { // TODO add method to kill server on closing the app
        start();
    }

    public void sendMessage(Message message) {
        byte[] buf = message.getContent().getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, message.getIp(), message.getPort());
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }
    }
}
