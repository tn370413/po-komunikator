package com.communicator490.controllers;

import com.communicator490.Communicator;
import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Controller {
    protected static Communicator communicator = Communicator.getInstance();

    protected void replace(Node a, Node b, @NotNull ObservableList<Node> siblings) {
        int pos = siblings.indexOf(a);
        siblings.remove(a);
        siblings.add(pos, b);
    }
}
