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

// Separate thread to (possibly) run I/O operations on file system without blocking the main application

public class LoggerThread extends Thread {
    private OutputStream outputStream;
//    LinkedList for FIFO
    private LinkedList<String> logMessages = new LinkedList<>();

//    defaults to stderr
    public LoggerThread(String name) {
        super(name);
        outputStream = System.err;
    }

    public LoggerThread(String name, String filename) {
        super(name);
        try {
            outputStream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            outputStream = System.err;
            logMessages.add("LOGGER ERROR: " + e.getMessage());
//            all code that is executed on the main application is run through Platform.runLater so that it is
//            run from the main thread
            Platform.runLater(
                    () -> Communicator.getInstance().handleWarning("Can't write to logfile, defaulting to stdout (" + e.getMessage() + ")")
            );
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while (!logMessages.isEmpty() && !Thread.interrupted()) {
//                we want a FIFO hence removeFirst is used
                String logMessage = logMessages.removeFirst();
                byte[] bytesToWrite = (logMessage + "\n").getBytes();
                try {
                    outputStream.write(bytesToWrite);
                } catch (IOException e) {
//                    we didn't print the message so we better save it back to print it in the next run of the loop
//                    when the outputStream is changed to something that works (stderr by default)
                    logMessages.addFirst(logMessage);
                    outputStream = System.err;
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
