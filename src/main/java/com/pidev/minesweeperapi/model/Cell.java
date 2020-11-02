package com.pidev.minesweeperapi.model;

import java.util.Objects;

/**
 * Represents a cell in the minesweeper game map.
 */
public class Cell {

    private final int row;
    private final int column;
    private final boolean mine;
    private final int minesAround;
    private boolean revealed;

    public Cell(final int row, final int column, final boolean mine, final int minesAround) {
        this.row = row;
        this.column = column;
        this.mine = mine;
        this.minesAround = minesAround;
        this.revealed = false;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isMine() {
        return mine;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cell cell = (Cell) o;
        return this.row == cell.row && this.column == cell.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
