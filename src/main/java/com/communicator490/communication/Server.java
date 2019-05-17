package com.communicator490.communication;

import com.communicator490.Communicator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    private DatagramSocket socket;
    private int internalPort = 490;
    private ServerThread serverThread;
    private SendingMessagesThread sendingThread;

    public void stop() {
        socket.close();
        serverThread.interrupt();
        sendingThread.interrupt();
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

        sendingThread = new SendingMessagesThread(socket, String.format("SendingServerOn%dThread", internalPort));
        sendingThread.start();
    }

    public Server() throws SocketException {
        start();
    }

    public void sendMessage(MessageToSend message) {
        sendingThread.send(message);
    }
}
