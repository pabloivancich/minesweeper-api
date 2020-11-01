package com.pidev.minesweeperapi.service;

import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Game Service.
 */
@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
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
     * @return the game created.
     */
    public Game create(Game game) {
        return gameRepository.save(game);
    }

}
