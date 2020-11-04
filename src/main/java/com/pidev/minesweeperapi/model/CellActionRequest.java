package com.pidev.minesweeperapi.model;

/**
 * Class that represents a request to action over a cell.
 */
public class CellActionRequest {

    private int row;
    private int column;
    private CellAction action;

    public CellActionRequest() {

    }

    public CellActionRequest(final int row, final int column, final CellAction action) {
        this.row = row;
        this.column = column;
        this.action = action;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public CellAction getAction() {
        return action;
    }
}
