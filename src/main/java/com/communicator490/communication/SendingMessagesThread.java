package com.communicator490.communication;

import com.communicator490.Communicator;
import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;

public class SendingMessagesThread extends Thread {
    private DatagramSocket socket;
    private LinkedList<MessageToSend> messages = new LinkedList<>();

    public SendingMessagesThread(DatagramSocket socket, String name) {
        super(name);
        this.socket = socket;
    }

    public void send(MessageToSend message) {
        this.messages.add(message);
    }

    public void run() {
        while (!Thread.interrupted()) {
            while (!messages.isEmpty() && !Thread.interrupted()) {
                MessageToSend message = messages.removeFirst();
                try {
                    byte[] buf = message.getContent().getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, message.getIp(), message.getPort());
                    socket.send(packet);
                    // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                    Platform.runLater(
                            () -> Communicator.getInstance().handleMessageSuccess(message)
                    );
                } catch (IOException e) {
                    Platform.runLater(
                            () -> Communicator.getInstance().handleMessageFailure(message, e.getMessage())
                    );
                }
            }
        }
    }
}
