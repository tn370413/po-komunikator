package com.communicator490.communication;

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
