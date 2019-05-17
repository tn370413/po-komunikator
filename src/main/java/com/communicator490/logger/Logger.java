package com.communicator490.logger;

import com.communicator490.Severity;

public class Logger {
    private LoggerThread loggerThread;

    public Logger() {
        loggerThread = new LoggerThread("LoggerThread");
        loggerThread.start();
    }

    public void stop() {
        loggerThread.interrupt();
    }

    public void log(String logMessage, Severity severity) {
        loggerThread.log(logMessage, severity);
    }
}
