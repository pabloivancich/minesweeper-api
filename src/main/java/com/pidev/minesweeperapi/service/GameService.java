package com.pidev.minesweeperapi.service;

import com.pidev.minesweeperapi.model.Cell;
import com.pidev.minesweeperapi.model.CellActionRequest;
import com.pidev.minesweeperapi.model.CellActionResponse;
import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameMap;
import com.pidev.minesweeperapi.model.GameState;
import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.processor.GameProcessor;
import com.pidev.minesweeperapi.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Game Service.
 */
@Service
public class GameService {

    private final GameRepository gameRepository;

    private final GameProcessor gameProcessor;

    @Autowired
    public GameService(GameRepository gameRepository, GameProcessor gameProcessor) {
        this.gameRepository = gameRepository;
        this.gameProcessor = gameProcessor;
    }

    /**
     * Retrieves the list of {@link Game}s for a given user.
     * @param user the user.
     * @return a list of games.
     */
    public List<Game> findByUser(User user) {
        List<Game> games = gameRepository.findByUser(user);
        Optional<Game> currentGame = gameProcessor.getUserCurrentGame(user.getId());

        currentGame.ifPresent(games::add);
        return games;
    }

    /**
     * Retrieves the {@link Game} with the given name of the given user.
     * If there is a current game, it will be replaced.
     * @param user the user.
     * @param gameName the game name.
     * @return the game.
     */
    public Optional<Game> findByUserAndName(User user, String gameName) {
        Optional<Game> gameOptional = gameRepository.findByUserAndName(user, gameName);
        gameOptional.ifPresent(game -> {
            game.setState(GameState.PLAYING);
            gameProcessor.storeGame(game);
        });
        return gameOptional;
    }

    /**
     * Creates a new {@link Game}.
     * @param game the game to be created.
     */
    public void create(Game game) {
        GameMap map = gameProcessor.createGameMap(
                game.getDifficulty(),
                game.getRows() != null ? Optional.of(game.getRows()) : Optional.empty(),
                game.getColumns() != null ? Optional.of(game.getColumns()) : Optional.empty(),
                game.getMines() != null ? Optional.of(game.getMines()) : Optional.empty()
        );
        Game gameCreated = new Game(
                game.getName(),
                game.getDifficulty(),
                game.getUser(),
                map
        );
        gameProcessor.storeGame(gameCreated);
    }

    /**
     * Saves a the current game for a given user.
     * @param userId the user id.
     */
    public void save(final Long userId) {
        Optional<Game> gameToBeSaved = gameProcessor.getUserCurrentGame(userId);
        gameToBeSaved.ifPresent(game -> {
            game.setState(GameState.SAVED);
            gameRepository.save(game);
        });
    }

    /**
     * Executes an action on a given cell in the current game for an user.
     * @param userId the user id.
     * @param cellActionRequest the cell action request.
     * @return a cell action response.
     */
    public CellActionResponse executeCellAction(final Long userId, final CellActionRequest cellActionRequest) {
        Optional<Game> game = gameProcessor.getUserCurrentGame(userId);

        List<Cell> cellsRevealed = new ArrayList<>();
        if(game.isPresent()) {
            switch (cellActionRequest.getAction()) {
                case REVEAL:
                    cellsRevealed = gameProcessor.revealCells(game.get(), cellActionRequest);
                    break;
                case QUESTION_MARK:
                    cellsRevealed = gameProcessor.markCellAsQuestionMark(game.get(), cellActionRequest);
                    break;
                case RED_FLAG:
                    cellsRevealed = gameProcessor.markCellAsRedFlag(game.get(), cellActionRequest);
                    break;
                default:
                    break;
            }
        }

        CellActionResponse response;
        switch(game.get().getState()) {
            case PLAYING: response = CellActionResponse.createCellsReavealedResponse(cellsRevealed);
                            break;
            case FINISHED_LOSE: response = CellActionResponse.createLoseResponse(cellsRevealed);
                            break;
            case FINISHED_WIN: response = CellActionResponse.createWinResponse(cellsRevealed);
                            break;
            default: response = null;
                        break;
        }

        return response;
    }

}
