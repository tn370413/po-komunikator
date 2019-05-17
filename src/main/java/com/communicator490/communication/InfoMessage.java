package com.communicator490.communication;

import com.communicator490.Severity;

// this class represents a meta-message from the application to the user

public class InfoMessage extends Message {
    private Severity severity;

    public InfoMessage(String content, Severity severity) {
        this.content = content;
        this.severity = severity;
    }

    public Severity getSeverity() {
        return severity;
    }
}
