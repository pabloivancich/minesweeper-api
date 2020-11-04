package com.pidev.minesweeperapi.model;

import java.util.ArrayList;
import java.util.List;

public class CellActionResponse {

    public static final String CELLS_REVEALED_MESSAGE = "{{quantity}} cells have been revealed";
    public static final String YOU_WIN_MESSAGE = "You Win!";
    public static final String YOU_LOSE_MESSAGE = "You Lose!";

    private String message;
    private List<Cell> cellsRevealed = new ArrayList<>();

    /**
     * Default constructos. For Jackson.
     */
    public CellActionResponse() {

    }

    private CellActionResponse(String message, List<Cell> cellsRevealed) {
        this.message = message;
        this.cellsRevealed = cellsRevealed;
    }

    public String getMessage() {
        return message;
    }

    public List<Cell> getCellsRevealed() {
        return cellsRevealed;
    }

    public static CellActionResponse createCellsReavealedResponse(List<Cell> cellsRevealed) {
        return new CellActionResponse(
                CELLS_REVEALED_MESSAGE.replace("{{quantity}}", String.valueOf(cellsRevealed.size())),
                cellsRevealed
        );
    }

    public static CellActionResponse createWinResponse(List<Cell> cellsRevealed) {
        return new CellActionResponse(
                YOU_WIN_MESSAGE,
                cellsRevealed
        );
    }

    public static CellActionResponse createLoseResponse(List<Cell> cellsRevealed) {
        return new CellActionResponse(
                YOU_LOSE_MESSAGE,
                cellsRevealed
        );
    }

}
