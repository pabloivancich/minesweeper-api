package com.pidev.minesweeperapi.processor;

import com.pidev.minesweeperapi.factory.GameMapFactory;
import com.pidev.minesweeperapi.model.Cell;
import com.pidev.minesweeperapi.model.CellActionRequest;
import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameDifficulty;
import com.pidev.minesweeperapi.model.GameMap;
import com.pidev.minesweeperapi.model.GameState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Timer;
import java.util.stream.Collectors;

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
     * @param game the game
     * @param cellActionRequest the cell action request.
     * @return a list of cells revealed.
     */
    public List<Cell> revealCells(Game game, CellActionRequest cellActionRequest) {
        Cell cell = game.getMap().getCells().get(cellActionRequest.getRow()).get(cellActionRequest.getColumn());
        game.addMove();

        // Check if cell is a mine and return the list of mines.
        if(cell.isMine()) {
            cell.setRevealed(true);
            game.setState(GameState.FINISHED_LOSE);
            return getMinesList(game);
        }

        // Reveal neighbour cells with 0 mines around.
        this.revealCells(game, cell);

        if(hasWon(game)) {
            game.setState(GameState.FINISHED_WIN);
        }

        return this.getRevealedCells(game);
    }

    /**
     * Reveals all the neighbour cells.
     * @param game the game
     * @param cell the cell to be revealed
     */
    private void revealCells(Game game, Cell cell) {
        if(cell.getMinesAround() == 0 && !cell.isRevealed()) {
            cell.setRevealed(true);
            List<Cell> neighbourCells = findNeighboursCells(game.getMap(), cell);
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
    public List<Cell> markCellAsQuestionMark(Game game, CellActionRequest cellActionRequest) {
        Cell cell = game.getMap().getCells().get(cellActionRequest.getRow()).get(cellActionRequest.getColumn());
        game.addMove();
        cell.setQuestionMark(!cell.isQuestionMark());
        return List.of(cell);
    }

    /**
     * Marks a cell as red flag.
     * @param cellActionRequest the cell action request.
     * @return a list with the cell marked as red flag.
     */
    public List<Cell> markCellAsRedFlag(Game game, CellActionRequest cellActionRequest) {
        Cell cell = game.getMap().getCells().get(cellActionRequest.getRow()).get(cellActionRequest.getColumn());
        game.addMove();

        if (cell.isRedFlag()) {
            cell.setRedFlag(false);
            game.removeRedFlag();
        } else {
            cell.setRedFlag(true);
            game.addRedFlag();
        }

        return List.of(cell);
    }

    /**
     * Retrieves the list of mines of a given game.
     * @return the list of mines.
     */
    private List<Cell> getMinesList(Game game) {
        List<Cell> mines = new ArrayList<>();

        game.getMap().getCells().forEach(row -> {
            mines.addAll(row.stream().filter(Cell::isMine).collect(Collectors.toList()));
        });

        return mines;
    }

    /**
     * Retrieves the list of revealed cells of a given game.
     * @return the list of revealed cells.
     */
    private List<Cell> getRevealedCells(Game game) {
        List<Cell> revealedCells = new ArrayList<>();

        game.getMap().getCells().forEach(row -> {
            revealedCells.addAll(row.stream().filter(Cell::isRevealed).collect(Collectors.toList()));
        });

        return revealedCells;
    }

    /**
     * Retrieves the list of red flags cells of a given game.
     * @return the list of revealed cells.
     */
    private List<Cell> getRedFlags(Game game) {
        List<Cell> redFlags = new ArrayList<>();

        game.getMap().getCells().forEach(row -> {
            redFlags.addAll(row.stream().filter(Cell::isRedFlag).collect(Collectors.toList()));
        });

        return redFlags;
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

        int revealed = getRevealedCells(game).size();

        int redFlags = game.getRedFlags();

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

        return (mapMines == redFlags) && this.getMinesList(game).containsAll(getRedFlags(game));
    }

}
