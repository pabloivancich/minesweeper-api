package com.pidev.minesweeperapi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a minesweeper game map.
 */
public class GameMap implements Serializable {

    private int rows;
    private int columns;
    private int mines;

    private List<List<Cell>> cells;

    public GameMap() {

    }

    public GameMap(final int rows, final int columns, final int mines, final List<List<Cell>> cells) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.cells = cells;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getMines() {
        return mines;
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

}
