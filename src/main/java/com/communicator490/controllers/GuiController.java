package com.communicator490.controllers;

import com.communicator490.communication.Communicator;

public class GuiController {
    protected static Communicator communicator;

    public static void setCommunicator(Communicator communicator) {
        GuiController.communicator = communicator;
    }
}
