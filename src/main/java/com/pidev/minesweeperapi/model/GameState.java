package com.pidev.minesweeperapi.model;

import java.util.stream.Stream;

/**
 * Enumeration that represents a minesweeper game state.
 */
public enum GameState {

    PLAYING("PLAYING"),
    SAVED("SAVED"),
    FINISHED_WIN("FINISHED_WIN"),
    FINISHED_LOSE("FINISHED_LOSE");

    private final String state;

    GameState(String state) {
        this.state = state;
    }

    public String state() {
        return this.state;
    }

    public static GameState getGameState(String state) throws IllegalArgumentException {
        return Stream.of(GameState.values())
                .filter(gs -> gs.state().equalsIgnoreCase(state))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
