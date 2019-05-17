package com.communicator490.communication;

import com.communicator490.Communicator;
import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerThread extends Thread {
    private DatagramSocket socket;

    public ServerThread(DatagramSocket socket, String name) {
        super(name);
        this.socket = socket;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                byte[] buf = new byte[32768];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                MessageReceived message = new MessageReceived(received, packet.getAddress(), packet.getPort());
                // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                Platform.runLater(
                    () -> Communicator.getInstance().receiveMessage(message)
                );
            } catch (IOException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
