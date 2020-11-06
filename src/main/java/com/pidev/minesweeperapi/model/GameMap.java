package com.pidev.minesweeperapi.model;

import com.pidev.minesweeperapi.exception.InvalidCellException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Retrieves the {@link Cell} of the position row,column
     * @param row the row.
     * @param column the column.
     * @return a cell.
     */
    public Cell getCell(int row, int column) throws InvalidCellException {

        if(row < 0 || row >= getRows() || column < 0 || column >= getColumns()) {
            throw new InvalidCellException("The cell (" + row + "," + column +" ) is not valid");
        }

        return cells.get(row).get(column);
    }

    /**
     * Retrieves the list of mines.
     * @return the list of mines.
     */
    public List<Cell> retrieveMinesList() {
        List<Cell> mines = new ArrayList<>();

        getCells().forEach(row -> {
            mines.addAll(row.stream().filter(Cell::isMine).collect(Collectors.toList()));
        });

        return mines;
    }

    /**
     * Retrieves the list of revealed cells.
     * @return the list of revealed cells.
     */
    public List<Cell> retrieveRevealedCellsList() {
        List<Cell> revealedCells = new ArrayList<>();

        getCells().forEach(row -> {
            revealedCells.addAll(row.stream().filter(Cell::isRevealed).collect(Collectors.toList()));
        });

        return revealedCells;
    }

    /**
     * Retrieves the list of red flags cells.
     * @return the list of revealed cells.
     */
    public List<Cell> retrieveRedFlagsList() {
        List<Cell> redFlags = new ArrayList<>();

        getCells().forEach(row -> {
            redFlags.addAll(row.stream().filter(Cell::isRedFlag).collect(Collectors.toList()));
        });

        return redFlags;
    }

    /**
     * Retrieves a list of neighbour {@link Cell}s of the given cell.
     * @param cell the cell.
     * @return a list of neighbour cells.
     */
    public List<Cell> findNeighboursCells(Cell cell) {
        List<Cell> neighbourCells = new ArrayList<>();
        int cellRow = cell.getRow();
        int cellColumn = cell.getColumn();

        for(int iRow = cellRow-1; iRow <= cellRow+1 ; iRow ++) {
            for(int jCol = cellColumn-1; jCol <= cellColumn+1 ; jCol ++) {
                if(iRow >=0 && iRow < getRows() && jCol >= 0 && jCol < getColumns()) {
                    Cell mapCell = null;
                    try {
                        mapCell = getCell(iRow, jCol);
                    } catch (InvalidCellException e) {
                        // this exception is controlled by the list iteration
                    }
                    assert mapCell != null;
                    if(!mapCell.equals(cell)) {
                        neighbourCells.add(mapCell);
                    }
                }
            }
        }

        return neighbourCells;
    }

    /**
     * Retrieves the quantity of mines around a given cell.
     * @param cell the cell.
     * @return the quantity of mines around the given cell.
     */
    public int findNeighboursMines(Cell cell) {
        List<Cell> neighboursCells = this.findNeighboursCells(cell);
        return (int) neighboursCells.stream().filter(Cell::isMine).count();
    }

}
