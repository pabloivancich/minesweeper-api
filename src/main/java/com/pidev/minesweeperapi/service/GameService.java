package com.pidev.minesweeperapi.service;

import com.pidev.minesweeperapi.factory.GameMapFactory;
import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameMap;
import com.pidev.minesweeperapi.model.GameState;
import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.processor.GameProcessor;
import com.pidev.minesweeperapi.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
        return gameRepository.findByUser(user);
    }

    /**
     * Retrieves the {@link Game} with the given name of the given user.
     * @param user the user.
     * @param gameName the game name.
     * @return the game.
     */
    public Optional<Game> findByUserAndName(User user, String gameName) {
        return gameRepository.findByUserAndName(user, gameName);
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

}
