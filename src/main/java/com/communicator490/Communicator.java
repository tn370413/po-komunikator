package com.communicator490;

import com.communicator490.communication.Conversation;
import com.communicator490.communication.Message;
import com.communicator490.communication.MessageReceived;
import com.communicator490.communication.MessageToSend;
import com.communicator490.communication.Server;
import com.communicator490.controllers.conversationWindow.ConversationWindowController;
import com.communicator490.controllers.mainWindow.MainWindowController;
import com.communicator490.logger.Logger;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

// This is a singleton that represents the application itself

// The communicator application consists of:
//  GUI:
//      Main window used to initiate connections and to inform user about application-level events
//      Conversation Windows identified by the IP of the other end
//  GUI controllers – to separate the view from "logic"
//  In the background:
//      Server representing part of the application that sends and receives data over network
//      Logger for logging data to stdout or to a file
//  main application controller (this class), which connects GUI controllers, logger and server

public class Communicator {
    private Server server;
    private Logger logger;
    private MainWindowController mainWindowController;
    private HashMap<String, ConversationWindowController> conversationWindowControllers = new HashMap<>();

    //    this map allows sending message to appropriate controller when the server notifies us about a change in
    //    message status due to some network event or something else
    private HashMap<Message, ConversationWindowController> messagesToControllersMap = new HashMap<>();

    //    creating the singleton
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

    public void stop() {
        server.stop();
        logger.log("Communicator stop", Severity.NOTE);
        logger.stop();
    }

    // these methods are not yet implemented;
    // they are supposed to allow for communication on the internet even if the client is behind a NAT
    // the idea is to use UDP hole punching, with a small server running somewhere on the internet
    // so-far tests with a server on students.mimuw.edu.pl were unsuccessful for unknown reasons
    // I didn't spend more time investigating why since this feature is not required in the specification for this task
//    public int getPort() {
//        return -1; // TODO communication through NAT (external)
//    }
//
//    public String getIp() {
//        return "???"; // TODO communication through NAT (external)
//    }

    public int getInternalPort() {
        return server.getInternalPort();
    }

    public String getInternalIp() {
        String ip = "???";

        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            handleWarning("Can't establish local IP: " + e.getMessage());
        }
        return ip;
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

            // we will later identify conversations by the IP of the other end;
            // we put a reference to the controller since Conversation as part of the 'logic' does not have a way
            // to interact with the GUI on its own, it is the controller that has access to both
            // Conversation and GUI (ConvesationWindow)
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

    public void endConversation(Conversation conversation) {
        conversationWindowControllers.remove(conversation.getForeignAddress());
    }

    public void sendMessage(MessageToSend message, ConversationWindowController windowController) {
        server.sendMessage(message);
//        we need to remember who (which window) sent the message to be able to inform the sender if something
//        happens to the message (is sent successfully or fails to be sent)
        messagesToControllersMap.put(message, windowController);
    }

    private void handleFatalError(String content) {
//        here fatal means an error that prevent the whole application from running
        for (Map.Entry<String, ConversationWindowController> entry : conversationWindowControllers.entrySet()) {
            entry.getValue().handleFatalError(content);
        }
        logger.log(content, Severity.ERROR);
        mainWindowController.handleFatalError(content);
        server.stop();
    }

    public void handleWarning(String content) {
        logger.log(content, Severity.WARNING);
        mainWindowController.handleWarning(content);
        notifyUser();
    }

    public void handleMessageFailure(MessageToSend message, String reason) {
        logger.log("Message to: " + message.getIp().getHostAddress() + " -- " + reason, Severity.WARNING);
//        remove messaege from the map since there is nothing that can happen to a failed message
        ConversationWindowController conversationWindowController = messagesToControllersMap.remove(message);
        conversationWindowController.handleSendError(message, reason);
    }

    public void handleMessageSuccess(MessageToSend message) {
        logger.log("Message to: " + message.getIp().getHostAddress() + " -- sent", Severity.NOTE);
//        remove messaege from the map since there is nothing that can happen to a sent message
        ConversationWindowController conversationWindowController = messagesToControllersMap.remove(message);
        conversationWindowController.handleSendSuccess(message);
    }

    public void notifyUser() {
//        ideally it should be a system notification
//        but for some reason even though its java there seems to be no standard way to make system notifications
//        on different platforms in a nice and consistent way, so for now we will be stuck with a beep
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public Logger getLogger() {
//        we expose the logger in the singleton so that everything can complain about what they want
        return logger;
    }
}
