package com.communicator490.controllers.conversationWindow;

import com.communicator490.Communicator;
import com.communicator490.communication.Conversation;
import com.communicator490.communication.Message;
import com.communicator490.controllers.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ConversationWindowController extends Controller {
    @FXML
    private Label headerLabel;

    @FXML
    private MessagesOuterBoxController messagesOuterBoxController;

    @FXML
    private TextField messageToSend;

    @FXML
    private Button sendButton;

    private Stage stage;
    private Conversation conversation;

    private void sendMessage(Message message) {
        try {
            Communicator.getInstance().sendMessage(message);
            messagesOuterBoxController.displayNewMessage(message.getContent(), "sent-message");
        } catch (IOException e) {
            messagesOuterBoxController.displayNewMessage(message.getContent(), "sent-message-failed");
            handleWarning("Error sending message: " + e.getMessage());
        }
    }

    public void initialize() {
        sendButton.setOnAction(actionEvent -> {
            Message message = new Message(messageToSend.getText(), conversation.getForeignAddress(), conversation.getForeignPort());
            sendMessage(message);
            messageToSend.setText("");
        });

        messageToSend.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendButton.fire();
                keyEvent.consume();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Conversation");
        stage.setOnCloseRequest(closeWindowHandler);
    }

    private EventHandler<WindowEvent> closeWindowHandler = windowEvent -> Communicator.getInstance().endConversation(this);

    public void receiveMessage(String content) {
        messagesOuterBoxController.displayNewMessage(content, "received-message");
        notifyUser();
    }

    private void notifyUser() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public void setConversation(Conversation conversation) {
        headerLabel.setText("Conversation with " + conversation.getForeignAddress() + String.format(" (port %d)", conversation.getForeignPort()));
        this.conversation = conversation;
        this.stage.setTitle("Conversation with " + conversation.getForeignAddress());
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void open() {
        stage.setIconified(false);
    }

    private void handleWarning(String message) {
        messagesOuterBoxController.displayNewMessage(message, "warning");
    }

    public void handleFatalError(String message) {
        messagesOuterBoxController.displayNewMessage(message, "error");
        sendButton.setOnAction(actionEvent -> {});
    }
}
