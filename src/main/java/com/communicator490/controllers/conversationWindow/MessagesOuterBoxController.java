package com.communicator490.controllers.conversationWindow;

import com.communicator490.Communicator;
import com.communicator490.communication.*;
import com.communicator490.controllers.Controller;
import com.communicator490.customControls.MessageControl;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class MessagesOuterBoxController extends Controller {
    @FXML
    private VBox messagesBox;

    @FXML
    private ScrollPane messagesBoxScrollPane;

    private HashMap<Message, MessageController> messageControllersMap = new HashMap<>();

    // Mesages → MessageControls HashMap?

    public void initialize() {
        MessageControl greeting = new MessageControl("Hello!");
        greeting.getStyleClass().add("greeting");
        messagesBox.getChildren().add(greeting);

        messagesBox.heightProperty().addListener(
                (observable, oldValue, newValue) -> {
                    messagesBox.layout();
                    messagesBoxScrollPane.setVvalue(1.0);
                }
        );
    }

    public void displayNewMessage(Message message) {
        String content = message.getContent();
        MessageControl messageControl = new MessageControl(content);
        String style = "note";
        if (message instanceof MessageReceived) {
            style = "received-message";
        } else if (message instanceof MessageToSend) {
            style = "sent-message";
        } else if (message instanceof InfoMessage) {
            Severity severity = ((InfoMessage) message).getSeverity();
            if (severity == Severity.NOTE) {
                style = "note";
            } else if (severity == Severity.WARNING) {
                style = "warning";
            } else if (severity == Severity.ERROR) {
                style = "error";
            }
        }
        messageControl.getStyleClass().add(style);
        MessageController messageController = messageControl.getController();
        messageControllersMap.put(message, messageController);
        messageController.bindWidth(messagesBoxScrollPane.widthProperty());
        messagesBox.getChildren().add(messageControl);
    }

    public void handleSendError(Message message, String reason) {
        InfoMessage infoMessage = new InfoMessage(reason, Severity.WARNING);
        displayNewMessage(infoMessage);
        MessageController messageController = messageControllersMap.get(message);
        messageController.setFailed();
    }

    public void handleSendSuccess(Message message) {
        MessageController messageController = messageControllersMap.get(message);
        messageController.setSuccess();
    }
}
