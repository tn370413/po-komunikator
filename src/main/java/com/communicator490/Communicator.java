package com.communicator490;

import com.communicator490.communication.Conversation;
import com.communicator490.communication.Message;
import com.communicator490.communication.MessageReceived;
import com.communicator490.communication.MessageToSend;
import com.communicator490.communication.Server;
import com.communicator490.controllers.conversationWindow.ConversationWindowController;
import com.communicator490.controllers.conversationWindow.MessageController;
import com.communicator490.controllers.mainWindow.MainWindowController;
import com.communicator490.logger.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Communicator {
    private Server server;
    private Logger logger;
    private MainWindowController mainWindowController;
    private HashMap<String, ConversationWindowController> conversationWindowControllers = new HashMap<>();
    private HashMap<Message, ConversationWindowController> messagesToControllersMap = new HashMap<>();

    private static Communicator communicator = new Communicator();

    public static Communicator getInstance() {
        return communicator;
    }

    private Communicator() {
        try {
            this.server = new Server();
        } catch (SocketException e) {
            handleFatalError("Can't start server: " + e.getMessage());
        }
        this.logger = new Logger();
        logger.log("Communicator start", Severity.NOTE);
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public int getPort() {
        return -1; // TODO communication through NAT (external)
    }

    public int getInternalPort() {
        return server.getInternalPort();
    }

    public String getIp() {
        return "???"; // TODO communication through NAT (external)
    }

    public String getInternalIp() {
        String ip = "???";

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            handleWarning("Can't establish local IP: " + e.getMessage());
        }
        return ip;
    }

    public void stop() {
        server.stop();
        logger.log("Communicator stop", Severity.NOTE);
        logger.stop();
    }

    public void openConversation(String ip, int port) {
        if (!conversationWindowControllers.containsKey(ip)) {
            Conversation conversation;
            ConversationWindowController conversationWindowController = null;
            try {
                conversation = new Conversation(InetAddress.getByName(ip), port);
                conversationWindowController = mainWindowController.openConversationWindow(conversation);
            } catch (UnknownHostException e) {
                mainWindowController.handleWarning("Couldn't open conversation: " + e.getMessage());
            }

            if (conversationWindowController != null) {
                conversationWindowControllers.put(ip, conversationWindowController);
            }
        } else {
            conversationWindowControllers.get(ip).open();
        }
    }

    public void receiveMessage(MessageReceived m) {
        InetAddress fromIP = m.getIp();
        logger.log("Message from: " + fromIP.getHostAddress() + " -- received", Severity.NOTE);
        openConversation(fromIP.getHostAddress(), m.getPort());
        conversationWindowControllers.get(fromIP.getHostAddress()).receiveMessage(m);
    }

    public void endConversation(ConversationWindowController windowController) {
        Communicator.getInstance().getLogger().log(windowController.getTitle() + ": Conversation stop", Severity.NOTE);
        conversationWindowControllers.remove(windowController.getConversation().getForeignAddress());
    }

    public void sendMessage(MessageToSend message, ConversationWindowController windowController) {
        server.sendMessage(message);
        messagesToControllersMap.put(message, windowController);
    }

    private void handleFatalError(String message) {
        for (Map.Entry<String, ConversationWindowController> entry : conversationWindowControllers.entrySet()) {
            entry.getValue().handleFatalError(message);
        }
        logger.log(message, Severity.ERROR);
        mainWindowController.handleFatalError(message);
        server.stop();
    }

    public void handleWarning(String message) {
        logger.log(message, Severity.WARNING);
        mainWindowController.handleWarning(message);
        notifyUser();
    }

    public void handleMessageFailure(MessageToSend message, String reason) {
        logger.log("Message to: " + message.getIp().getHostAddress() + " -- " + reason, Severity.WARNING);
        ConversationWindowController conversationWindowController = messagesToControllersMap.remove(message);
        conversationWindowController.handleSendError(message, reason);
    }

    public void handleMessageSuccess(Message message) {
        logger.log("Message to: " + message.getIp().getHostAddress() + " -- sent", Severity.NOTE);
        ConversationWindowController conversationWindowController = messagesToControllersMap.remove(message);
        conversationWindowController.handleSendSuccess(message);
    }

    public void notifyUser() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public Logger getLogger() {
        return logger;
    }
}
