package com.communicator490.controllers.conversationWindow;

import com.communicator490.Severity;
import com.communicator490.communication.*;
import com.communicator490.controllers.Controller;
import com.communicator490.customControls.MessageControl;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class MessagesOuterBoxController extends Controller {
    @FXML
    private VBox messagesBox;

    @FXML
    private ScrollPane messagesBoxScrollPane;

    private HashMap<Message, MessageController> messageControllersMap = new HashMap<>();

    public void initialize() {
//        inspired by Facebook's "Now you can talk with XYZ"
        MessageControl greeting = new MessageControl("Hello!");
        greeting.getStyleClass().add("greeting");
        messagesBox.getChildren().add(greeting);

//        for some reason autoscroll set on adding an element to the box, or manually in displayNewMessage
//        doesn't work, fortunately this works
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

        messageControl.getStyleClass().add(MessageController.getStyle(message));

        MessageController messageController = messageControl.getController();
        messageControllersMap.put(message, messageController);

//        this is needed to make message text wrap correctly
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
