package com.communicator490.communication;

import com.communicator490.Communicator;
import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerThread extends Thread {
    private DatagramSocket socket;
    private int internalPort;

    public ServerThread(int internalPort) throws SocketException {
        super(String.format("ServerOn%dThread", internalPort));
        this.internalPort = internalPort;
        boolean success = false;
        while (!success && this.internalPort < 65536) {
            try {
                socket = new DatagramSocket(this.internalPort);
                success = true;
            } catch (SocketException e) {
                this.internalPort++;
            }
        }
        this.setName(String.format("ServerOn%dThread", this.internalPort));

        if (!success) {
            throw new SocketException(String.format("Couldn't start server: can't listen at internalPort %d or any higher", internalPort));
        }
    }

    public int getInternalPort() {
        return internalPort;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                byte[] buf = new byte[32768];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                Message message = new Message(received, packet.getAddress(), packet.getPort());
                // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                Platform.runLater(
                    () -> {
                        Communicator.getInstance().receiveMessage(message);
                    }
                );
            } catch (IOException e) {
                //e.printStackTrace(); // TODO
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopServerThread() {
        socket.close();
        Thread.currentThread().interrupt();
    }
}
