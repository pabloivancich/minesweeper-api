package com.pidev.minesweeperapi.model;

import java.util.stream.Stream;

/**
 * Enumeration that represents an action over a cell.
 */
public enum CellAction {

    REVEAL("REVEAL"),
    QUESTION_MARK("QUESTION_MARK"),
    RED_FLAG("RED_FLAG");

    private final String action;

    CellAction(String action) {
        this.action = action;
    }

    public String action() {
        return this.action;
    }

    public static CellAction getCellAction(String action) throws IllegalArgumentException {
        return Stream.of(CellAction.values())
                .filter(gs -> gs.action().equalsIgnoreCase(action))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
