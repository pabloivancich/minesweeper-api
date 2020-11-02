package com.pidev.minesweeperapi.factory;

import com.pidev.minesweeperapi.model.Cell;
import com.pidev.minesweeperapi.model.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Factory that creates Minesweeper {@link Map}s.
 */
public class MapFactory {

    /**
     * Generates a complete {@link Map} given the size and mines quantity.
     * @param rows the rows.
     * @param columns the columns.
     * @param mines the mines quantity.
     * @return a Map.
     */
    public static Map generate(final int rows, final int columns, final int mines) {
        List<Cell> minesList = buildMinesList(rows, columns, mines);
        System.out.println("Mines generated >> " + minesList.size());

        List<List<Cell>> cells = buildCellMatrix(rows, columns, minesList);

        return new Map(rows, columns, mines, cells);
    }

    /**
     * Builds a mines {@link Cell} list.
     * @param rows the rows where the mines are generated.
     * @param columns the columns where the mines are generated.
     * @param mines the mines quantity.
     * @return a list of mines.
     */
    private static List<Cell> buildMinesList(final int rows, final int columns, final int mines) {
        List<Cell> minesList = new ArrayList<>();

        // Insert the first one mine.
        minesList.add(generateRandomMine(rows, columns));

        // Iterate and generate the other mines.
        IntStream.range(1, mines).forEach(i -> {
            Cell mine = generateRandomMine(rows, columns);
            while(minesList.contains(mine)) {
                mine = generateRandomMine(rows, columns);
            }
            minesList.add(mine);
        });

        return minesList;
    }

    /**
     * Builds a {@link Cell} matrix.
     * @param rows the rows of the matrix.
     * @param columns the columns of the matrix.
     * @param minesList the mines list.
     * @return a Cell matrix.
     */
    private static List<List<Cell>> buildCellMatrix(final int rows, final int columns, final List<Cell> minesList) {
        List<List<Cell>> rowList = new ArrayList<>();

        IntStream.range(0, rows).forEach(row -> {
            List<Cell> columnList = new ArrayList<>();

            IntStream.range(0, columns).forEach(column -> {
                // Find if there is a mine or not.

                // Find the mines around.


                Cell cell = new Cell(row, column, true, 0);
                columnList.add(cell);
            });

            System.out.println("Columns generated >> " + columnList.size());
            rowList.add(columnList);
        });

        System.out.println("Rows generated >> " + rowList.size());

        return rowList;
    }

    /**
     * Generates a random mine {@link Cell}
     * @param maxRows the rows of the map.
     * @param maxColumns the columns of the map.
     * @return a Mine.
     */
    private static Cell generateRandomMine(final int maxRows, final int maxColumns) {
        return new Cell(
                generateRandomNumber(maxRows),
                generateRandomNumber(maxColumns),
                true,
                0
        );
    }

    /**
     * Generates a random number between 0 (inclusive) and MAX (exclusive).
     * @param max the maximum number (exclusive).
     * @return a random number.
     */
    private static int generateRandomNumber(final int max) {
        Random random = new Random();
        return random.ints(0, max)
                .findFirst()
                .getAsInt();
    }

}
