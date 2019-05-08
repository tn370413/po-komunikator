package com.communicator490.controllers.conversationWindow;

import com.communicator490.controllers.Controller;
import com.communicator490.customControls.MessageControl;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MessagesOuterBoxController extends Controller {
    @FXML
    private VBox messagesBox;

    @FXML
    private ScrollPane messagesBoxScrollPane;

    public void displayNewMessage(String content, String style) {
        MessageControl messageControl = new MessageControl(content);
        messageControl.getStyleClass().add(style);
        messageControl.getController().bindWidth(messagesBoxScrollPane.widthProperty());
        messagesBox.getChildren().add(messageControl);
        messagesBoxScrollPane.setVvalue(1.0);
    }
}
