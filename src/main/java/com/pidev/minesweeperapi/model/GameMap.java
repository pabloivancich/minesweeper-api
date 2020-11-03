package com.pidev.minesweeperapi.model;

import java.util.List;

/**
 * Represents a minesweeper game map.
 */
public class GameMap {

    private final int rows;
    private final int columns;
    private final int mines;

    private final List<List<Cell>> cells;

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
