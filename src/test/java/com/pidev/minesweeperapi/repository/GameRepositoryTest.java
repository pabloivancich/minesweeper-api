package com.pidev.minesweeperapi.repository;

import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameDifficulty;
import com.pidev.minesweeperapi.model.GameMap;
import com.pidev.minesweeperapi.model.GameState;
import com.pidev.minesweeperapi.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class GameRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        User user = new User("testUser");
        user = userRepository.save(user);

        Game gameToSave = new Game(
                "testGame",
                GameDifficulty.BEGINNER,
                user,
                new GameMap()
        );

        Game gameSaved = gameRepository.save(gameToSave);

        assertThat(gameSaved).isNotNull();
        assertThat(gameSaved.getName()).isEqualTo("testGame");
        assertThat(gameSaved.getTimePlayed()).isEqualTo(0);
        assertThat(gameSaved.getMinesRevealed()).isEqualTo(0);
        assertThat(gameSaved.getState()).isEqualTo(GameState.PLAYING);
        assertThat(gameSaved.getDifficulty()).isEqualTo(GameDifficulty.BEGINNER);
    }

    @Test
    public void testFindByUser() {
        User user = new User("testUser");
        user = userRepository.save(user);

        Game aGame = new Game(
                "aGame",
                GameDifficulty.BEGINNER,
                user,
                null
        );
        aGame = gameRepository.save(aGame);

        Game anotherGame = new Game(
                "anotherGame",
                GameDifficulty.INTERMEDIATE,
                user,
                null
        );
        anotherGame = gameRepository.save(anotherGame);

        User requestUser = new User(user.getId());
        List<Game> games = gameRepository.findByUser(requestUser);

        assertThat(games).isNotNull();
        assertThat(games.size()).isEqualTo(2);
    }

    @Test
    public void testFindByUserEmptyList() {
        User user = new User("testUser");
        user = userRepository.save(user);

        User anotherUser = new User("anotherTestUser");
        anotherUser = userRepository.save(anotherUser);

        Game aGame = new Game(
                "aGame",
                GameDifficulty.BEGINNER,
                user,
                null
        );
        aGame = gameRepository.save(aGame);

        Game anotherGame = new Game(
                "anotherGame",
                GameDifficulty.INTERMEDIATE,
                user,
                null
        );
        anotherGame = gameRepository.save(anotherGame);

        User requestUser = new User(anotherUser.getId());
        List<Game> games = gameRepository.findByUser(anotherUser);

        assertThat(games).isNotNull();
        assertThat(games.isEmpty()).isTrue();
    }

    @Test
    public void testFindByUserAndName() {
        User user = new User("testUser");
        user = userRepository.save(user);

        Game gameToSave = new Game(
                "testGame",
                GameDifficulty.BEGINNER,
                user,
                null
        );

        Game gameSaved = gameRepository.save(gameToSave);
        assertThat(gameSaved).isNotNull();
        assertThat(gameSaved.getName()).isEqualTo("testGame");

        Optional<Game> game = gameRepository.findByUserAndName(user, "testGame");
        assertThat(game.isPresent()).isTrue();
        assertThat(game.get().getName()).isEqualTo("testGame");
        assertThat(game.get().getUser().getName()).isEqualTo("testUser");
     }

    @Test
    public void testFindByUserAndNameNotFoundBecauseUser() {
        User user = new User("testUser");
        user = userRepository.save(user);

        Game gameToSave = new Game(
                "testGame",
                GameDifficulty.BEGINNER,
                user,
                null
        );
        gameRepository.save(gameToSave);

        User anotherUser = new User("anotherTestUser");
        anotherUser = userRepository.save(anotherUser);

        Optional<Game> game = gameRepository.findByUserAndName(anotherUser, "testGame");
        assertThat(game.isEmpty()).isTrue();
    }

    @Test
    public void testFindByUserAndNameNotFoundBecauseGameName() {
        User user = new User("testUser");
        user = userRepository.save(user);

        Optional<Game> game = gameRepository.findByUserAndName(user, "unknownGame");
        assertThat(game.isEmpty()).isTrue();
    }

}
