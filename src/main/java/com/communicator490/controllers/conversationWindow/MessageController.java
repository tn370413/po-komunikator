package com.communicator490.controllers.conversationWindow;

import com.communicator490.controllers.Controller;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageController extends Controller {
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

    public void setFailed() {
        messageContent.setFill(Paint.valueOf("gray"));
        messageContent.setStrikethrough(true);
    }

    public void setSuccess() {
        messageContent.setText("(✓) " + messageContent.getText());
    }
}
