package com.communicator490.controllers.conversationWindow;

import com.communicator490.Severity;
import com.communicator490.communication.InfoMessage;
import com.communicator490.communication.Message;
import com.communicator490.communication.MessageReceived;
import com.communicator490.communication.MessageToSend;
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
//        this is so that wrapping of text actually works, instead of longer lines making ScrollPane show horizontal
//        scrollbar
//        a bit of subtracting so that it doesn't "glue" itself to the side of the box
        messageContent.wrappingWidthProperty().bind(widthProperty.subtract(100));
    }

    public String getText() {
        return messageContent.getText();
    }

    public void setText(String s) {
        messageContent.setText(s);
    }

    public void setFailed() {
//        mark the message as failed-to-be-sent
        messageContent.setFill(Paint.valueOf("gray"));
        messageContent.setStrikethrough(true);
    }

    public void setSuccess() {
//        mark to the user that the message has been sent (I know that with UDP it doesn't mean much)
        messageContent.setText("(✓) " + messageContent.getText());
    }

    public static String getStyle(Message message) {
//        one could say it would be simpler to make a 'getStyle' method on Message class
//        but then that would break separation between GUI and logic:
//        the way it is now, if we were to make a completely new GUI with a completely different library
//        we could have 'communication' and 'logger' packages completely unchanged
//        unfortunately this results in this one piece of ugly ifs
//
//        another way would perhaps be to make MessageController know its message (like ConversationWindowController)
//        which would mean that when we create a MessageControl we need to pass to it not a String but a whole Message
//        so that it can create a MessageController with that Message
//        I do not like the idea of MessageControl knowing of Message since the latter is part of the 'logic'
//
//        the only other way I see is to make constructors of MessageControl and MessageController not take any args
//        and then set Message on MessageController separately and explicitly
//        then MessageController could set appropriate text on the Message
//
//        but even then, we would still end up with lots of ifs here, the difference being that the method would not be
//        static; imho it's a bit of work that is rather unnecessary, especially since we can reasonably expect
//        that anything wanting to display a message would also know that Message

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
        return style;
    }
}
