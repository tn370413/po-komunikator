package com.communicator490.controllers.conversationWindow;

import com.communicator490.Communicator;
import com.communicator490.Severity;
import com.communicator490.communication.*;
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
import java.net.UnknownHostException;

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

    private void sendMessage(MessageToSend message) {
        messagesOuterBoxController.displayNewMessage(message);
        Communicator.getInstance().sendMessage(message, this);
    }

    public void handleSendError(Message message, String reason) {
        messagesOuterBoxController.handleSendError(message, reason);
    }

    public void handleSendSuccess(Message message) {
        messagesOuterBoxController.handleSendSuccess(message);
    }

    public void initialize() {
        sendButton.setOnAction(actionEvent -> {
            MessageToSend message = null;
            try {
                message = new MessageToSend(messageToSend.getText(), conversation.getForeignAddress(), conversation.getForeignPort());
            } catch (UnknownHostException e) {
                handleWarning(e.getMessage());
            }
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

    public void receiveMessage(MessageReceived message) {
        messagesOuterBoxController.displayNewMessage(message);
        notifyUser();
    }

    private void notifyUser() {
        Communicator.getInstance().notifyUser();
    }

    public void setConversation(Conversation conversation) {
        headerLabel.setText("Conversation with " + conversation.getForeignAddress() + String.format(" (port %d)", conversation.getForeignPort()));
        this.conversation = conversation;
        stage.setTitle("Conversation with " + conversation.getForeignAddress());
        Communicator.getInstance().getLogger().log(stage.getTitle() + ": Conversation start", Severity.NOTE);
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void open() {
        stage.setIconified(false);
    }

    private void handleWarning(String content) {
        InfoMessage infoMessage = new InfoMessage(content, Severity.WARNING);
        messagesOuterBoxController.displayNewMessage(infoMessage);
        Communicator.getInstance().getLogger().log(stage.getTitle() + ": " + content, Severity.WARNING);
    }

    public void handleFatalError(String content) {
        InfoMessage message = new InfoMessage(content, Severity.ERROR);
        messagesOuterBoxController.displayNewMessage(message);
        sendButton.setOnAction(actionEvent -> {});
        Communicator.getInstance().getLogger().log(stage.getTitle() + ": " + content, Severity.ERROR);
    }

    public String getTitle() {
        return stage.getTitle();
    }
}
