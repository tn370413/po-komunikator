package com.communicator490.controllers.conversationWindow;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageController {
    @FXML
    private Label messageDate;

    @FXML
    private Text messageContent;

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

    public void bindWidth(ReadOnlyDoubleProperty widthProperty) {
        messageContent.wrappingWidthProperty().bind(widthProperty.subtract(100));
    }

    public String getText() {
        return messageContent.getText();
    }

    public void setText(String s) {
        messageContent.setText(s);
    }
}
