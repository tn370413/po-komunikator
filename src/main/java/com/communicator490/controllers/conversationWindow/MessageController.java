package com.communicator490.controllers.conversationWindow;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageController {
    @FXML
    private Label messageDate;

    @FXML
    private Label messageContent;

    private String content;

    public StringProperty textProperty() {
        return messageContent.textProperty();
    }

    public MessageController(String content) {
        this.content = content;
    }

    public void initialize() {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        messageDate.setText(dateFormat.format(now));

        messageContent.setText(this.content);
    }

    public String getText() {
        return messageContent.getText();
    }

    public void setText(String s) {
        messageContent.setText(s);
    }
}
