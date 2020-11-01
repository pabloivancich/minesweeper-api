package com.pidev.minesweeperapi.model;

import java.util.stream.Stream;

/**
 * Enumeration that represents a minesweeper game difficulty.
 */
public enum GameDifficulty {

    BEGINNER("BEGINNER", 8, 8, 10),
    INTERMEDIATE("INTERMEDIATE", 16, 16, 40),
    ADVANCED("ADVANCED", 16, 30, 99),
    CUSTOM("CUSTOM", 0, 0, 0);

    private final String difficulty;
    private final Integer rows;
    private final Integer columns;
    private final Integer mines;

    GameDifficulty(String difficulty, int rows, int columns, int mines) {
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public Integer getMines() {
        return mines;
    }

    public static GameDifficulty getGameDifficulty(final String difficulty) throws IllegalArgumentException {
        return Stream.of(GameDifficulty.values())
                .filter(gd -> gd.getDifficulty().equalsIgnoreCase(difficulty))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
