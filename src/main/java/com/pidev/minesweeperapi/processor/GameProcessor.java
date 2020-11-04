package com.pidev.minesweeperapi.processor;

import com.pidev.minesweeperapi.factory.GameMapFactory;
import com.pidev.minesweeperapi.model.Cell;
import com.pidev.minesweeperapi.model.CellActionRequest;
import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameDifficulty;
import com.pidev.minesweeperapi.model.GameMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class GameProcessor {

    /**
     * In memory map with the current games for each player.
     */
    private final Map<Long, Game> currentGames = new HashMap<Long, Game>();

    private Map<Long, Game> getCurrentGames() {
        return currentGames;
    }

    /**
     * Retrieves the current game for the given user.
     * @param userId the user id.
     * @return the game.
     */
    public Optional<Game> getUserCurrentGame(final Long userId) {
        Game currentGame = getCurrentGames().get(userId);
        return currentGame != null
                ? Optional.of(currentGame)
                : Optional.empty();
    }

    /**
     * Stores the current game for a given supplier.
     * If the suppliers already has a game in progress, it will be deleted.
     * @param game the new current game.
     */
    public void storeGame(Game game) {
        this.getCurrentGames().put(game.getUser().getId(), game);
    }

    public GameMap createGameMap(
            GameDifficulty difficulty,
            Optional<Integer> rows,
            Optional<Integer> columns,
            Optional<Integer> mines
    ) {
        // If the difficulty is CUSTOM, rows, columns and mines are mandatory.
        if(GameDifficulty.CUSTOM.equals(difficulty)) {
            // TODO here we can throw a catcheable exception.
            rows.orElseThrow(NoSuchElementException::new);
            columns.orElseThrow(NoSuchElementException::new);
            mines.orElseThrow(NoSuchElementException::new);
        }

        GameMap map = !GameDifficulty.CUSTOM.equals(difficulty)
                ? GameMapFactory.generate(difficulty.getRows(), difficulty.getColumns(), difficulty.getMines())
                : GameMapFactory.generate(rows.get(), columns.get(), mines.get()) ;

        map.getCells().forEach(columnList -> columnList.forEach(
                cell -> {
                    cell.setMinesAround(this.findNeighboursMines(map, cell));
                })
        );

        return map;
    }

    /**
     * Retrieves a list of neighbour {@link Cell}s of the given cell.
     * @param map the game map.
     * @param cell the cell.
     * @return a list of neighbour cells.
     */
    private List<Cell> findNeighboursCells(GameMap map, Cell cell) {
        List<Cell> neighbourCells = new ArrayList<>();
        int cellRow = cell.getRow();
        int cellColumn = cell.getColumn();

        for(int iRow = cellRow-1; iRow <= cellRow+1 ; iRow ++) {
            for(int jCol = cellColumn-1; jCol <= cellColumn+1 ; jCol ++) {
                if(iRow >=0 && iRow < map.getRows() && jCol >= 0 && jCol < map.getColumns()) {
                    Cell mapCell = map.getCells().get(iRow).get(jCol);
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
     * @param map the game map.
     * @param cell the cell.
     * @return the quantity of mines around the given cell.
     */
    private int findNeighboursMines(GameMap map, Cell cell) {
        List<Cell> neighboursCells = findNeighboursCells(map, cell);
        return (int) neighboursCells.stream().filter(Cell::isMine).count();
    }

    /**
     * Reveal a {@link Cell}.
     * When a cell with no adjacent mines is revealed, all adjacent squares will be revealed (and repeat).
     *
     * @param cellActionRequest
     * @return a list of cells revealed.
     */
    public List<Cell> revealCell(Game game, CellActionRequest cellActionRequest) {
        return null;
    }

    /**
     *
     * @param cellActionRequest
     * @return
     */
    public List<Cell> markCellAsQuestionMark(CellActionRequest cellActionRequest) {
        return null;
    }

    /**
     *
     * @param cellActionRequest
     * @return
     */
    public List<Cell> markCellAsRedFlag(CellActionRequest cellActionRequest) {
        return null;
    }



}
