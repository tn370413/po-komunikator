package com.communicator490.logger;

import com.communicator490.Communicator;
import com.communicator490.Severity;
import javafx.application.Platform;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;

public class LoggerThread extends Thread {
    private OutputStream outputStream;
    private LinkedList<String> logMessages = new LinkedList<>();

    public LoggerThread(String name) {
        super(name);
        outputStream = System.out;
    }

    public LoggerThread(String name, String filename) {
        super(name);
        try {
            outputStream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            outputStream = System.out;
            logMessages.add("LOGGER ERROR: " + e.getMessage());
            Platform.runLater(
                    () -> Communicator.getInstance().handleWarning("Can't write to logfile, defaulting to stdout (" + e.getMessage() + ")")
            );
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while (!logMessages.isEmpty() && !Thread.interrupted()) {
                String logMessage = logMessages.removeFirst();
                byte[] bytesToWrite = (logMessage + "\n").getBytes();
                try {
                    outputStream.write(bytesToWrite);
                } catch (IOException e) {
                    logMessages.addFirst(logMessage);
                    outputStream = System.out;
                    Platform.runLater(
                            () -> Communicator.getInstance().handleWarning("Can't write to logfile: " + e.getMessage())
                    );
                }
            }
        }
    }

    public void log(String logMessage, Severity severity) {
        Date now = new Date();
        logMessages.add(now.toString() + ": [" + severity.toString() + "] " + logMessage);
    }
}
