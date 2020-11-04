package com.pidev.minesweeperapi.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a cell in the minesweeper game map.
 */
public class Cell implements Serializable {

    private int row;
    private int column;
    private boolean mine;
    private int minesAround;
    private boolean revealed;
    private boolean marked;

    public Cell() {

    }

    public Cell(final int row, final int column, final boolean mine) {
        this.row = row;
        this.column = column;
        this.mine = mine;
        this.minesAround = 0;
        this.revealed = false;
        this.marked = false;
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

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
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
