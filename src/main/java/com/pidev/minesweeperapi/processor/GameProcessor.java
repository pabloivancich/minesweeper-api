package com.pidev.minesweeperapi.processor;

import com.pidev.minesweeperapi.exception.InvalidCellException;
import com.pidev.minesweeperapi.factory.GameMapFactory;
import com.pidev.minesweeperapi.model.Cell;
import com.pidev.minesweeperapi.controller.dto.CellActionRequest;
import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameDifficulty;
import com.pidev.minesweeperapi.model.GameMap;
import com.pidev.minesweeperapi.model.GameState;
import com.pidev.minesweeperapi.util.Timer;
import org.springframework.stereotype.Component;

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

    /**
     * In memory timers for the current games for each player.
     */
    private final Map<Long, Timer> currentGamesTimers = new HashMap<>();

    /**
     * Retrieves the current games map.
     * @return a map.
     */
    private Map<Long, Game> getCurrentGames() {
        return currentGames;
    }

    /**
     * Retrieves the current games timers map.
     * @return a map.
     */
    private Map<Long, Timer> getCurrentGamesTimers() {
        return currentGamesTimers;
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
     * Retrieves the current game timer for the given user.
     * @param userId the user id.
     * @return the game timer.
     */
    private Optional<Timer> getUserCurrentGameTimer(final Long userId) {
        Timer timer =getCurrentGamesTimers().get(userId);
        return timer != null
                ? Optional.of(timer)
                : Optional.empty();
    }

    /**
     * Stores the current game for a given supplier.
     * If the suppliers already has a game in progress, it will be deleted.
     * @param game the new current game.
     */
    public void storeGame(Game game) {
        Timer timer = new Timer();
        getCurrentGamesTimers().put(game.getUser().getId(), timer);
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
                    cell.setMinesAround(map.findNeighboursMines(cell));
                })
        );

        return map;
    }

    /**
     * Reveal a {@link Cell}.
     * When a cell with no adjacent mines is revealed, all adjacent squares will be revealed (and repeat).
     *
     * @param game the game
     * @param cellActionRequest the cell action request.
     * @return a list of cells revealed.
     */
    public List<Cell> revealCells(Game game, CellActionRequest cellActionRequest) throws InvalidCellException {
        Cell cell = game.getMap().getCell(cellActionRequest.getRow(),cellActionRequest.getColumn());
        game.addMove();

        // Check if cell is a mine and return the list of mines.
        if(cell.isMine()) {
            cell.setRevealed(true);
            game.setState(GameState.FINISHED_LOSE);
            return game.getMap().retrieveMinesList();
        }

        // Reveal neighbour cells with 0 mines around.
        this.revealCells(game, cell);

        if(hasWon(game)) {
            game.setState(GameState.FINISHED_WIN);
        }

        // Adding seconds to the time played.
        Optional<Timer> timer = getUserCurrentGameTimer(game.getUser().getId());
        timer.ifPresent(value -> {
            game.addTimePlayed(value.total());
            value.restart();
        });

        List<Cell> cellsRevealed = game.getMap().retrieveRevealedCellsList();
        game.setCellsRevealed(cellsRevealed.size());
        return cellsRevealed;
    }

    /**
     * Reveals all the neighbour cells.
     * @param game the game
     * @param cell the cell to be revealed
     */
    private void revealCells(Game game, Cell cell) throws InvalidCellException {
        if(cell.getMinesAround() == 0 && !cell.isRevealed()) {
            cell.setRevealed(true);
            List<Cell> neighbourCells = game.getMap().findNeighboursCells(cell);
            for(Cell neighbourCell: neighbourCells) {
                revealCells(game, neighbourCell);
            }
        } else if(!cell.isRevealed()) {
            cell.setRevealed(true);
        }
    }

    /**
     * Marks a cell as question mark
     * @param cellActionRequest the cell action request.
     * @return a list with the cell marked as question mark.
     */
    public List<Cell> markCellAsQuestionMark(Game game, CellActionRequest cellActionRequest)
            throws InvalidCellException {
        Cell cell = game.getMap().getCell(cellActionRequest.getRow(),cellActionRequest.getColumn());
        game.addMove();
        cell.setQuestionMark(!cell.isQuestionMark());

        // Adding seconds to the time played.
        Optional<Timer> timer = getUserCurrentGameTimer(game.getUser().getId());
        timer.ifPresent(value -> {
            game.addTimePlayed(value.total());
            value.restart();
        });

        return List.of(cell);
    }

    /**
     * Marks a cell as red flag.
     * @param cellActionRequest the cell action request.
     * @return a list with the cell marked as red flag.
     */
    public List<Cell> markCellAsRedFlag(Game game, CellActionRequest cellActionRequest)
            throws InvalidCellException {
        Cell cell = game.getMap().getCell(cellActionRequest.getRow(),cellActionRequest.getColumn());
        game.addMove();

        if (cell.isRedFlag()) {
            cell.setRedFlag(false);
            game.removeRedFlag();
        } else {
            cell.setRedFlag(true);
            game.addRedFlag();
        }

        // Adding seconds to the time played.
        Optional<Timer> timer = getUserCurrentGameTimer(game.getUser().getId());
        timer.ifPresent(value -> {
            game.addTimePlayed(value.total());
            value.restart();
        });

        return List.of(cell);
    }

    /**
     * Determines if the game has been won.
     * @param game the game.
     * @return true if the game has been won or false otherwise.
     */
    private boolean hasWon(Game game) {
        int rows = game.getMap().getRows();
        int columns = game.getMap().getColumns();
        int mapMines = game.getMap().getMines();

        int revealed = game.getMap().retrieveRevealedCellsList().size();

        return ((rows * columns) - mapMines) == revealed
                && areAllMinesDiscovered(game);
    }

    /**
     * Determines if all the mines are marked as red flags.
     * @param game the game.
     * @return true if all the mines are marked as red flags.
     */
    private boolean areAllMinesDiscovered(Game game) {
        int mapMines = game.getMap().getMines();
        int redFlags = game.getRedFlags();

        List<Cell> minesList = game.getMap().retrieveMinesList();
        List<Cell> redFlagsList = game.getMap().retrieveRedFlagsList();

        return (mapMines == redFlags) && minesList.containsAll(redFlagsList);
    }

}
