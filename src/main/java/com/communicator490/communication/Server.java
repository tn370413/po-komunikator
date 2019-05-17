package com.communicator490.communication;

import java.net.DatagramSocket;
import java.net.SocketException;

// Class that represents all networking operations of the Communicator

public class Server {
    private DatagramSocket socket;
    private int internalPort = 490; // internal in opposition to externalPort visible in the outer internet
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
//        searching for an available port to open the UDP socket
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

//        once we have a port (and a socket), we can start threads for receiving and sending messages
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
