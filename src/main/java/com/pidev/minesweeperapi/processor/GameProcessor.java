package com.pidev.minesweeperapi.processor;

import com.pidev.minesweeperapi.model.Cell;
import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GameProcessor {

    /**
     * In memory map with the current games for each player.
     */
    private final Map<Long, Game> currentGames = new HashMap<Long, Game>();


    public Map<Long, Game> getCurrentGames() {
        return currentGames;
    }

    public static List<Cell> findNeighboursCells(GameMap map, Cell cell) {
        return List.of();
    }

    /**
     * Retrieves the quantity of mines around a given cell.
     * @param map the game map.
     * @param cell the cell.
     * @return the quantity of mines around the given cell.
     */
    public static int findNeighboursMines(GameMap map, Cell cell) {
        List<Cell> neighboursCells = findNeighboursCells(map, cell);
        return (int) neighboursCells.stream().filter(Cell::isMine).count();
    }

}
