package com.pidev.minesweeperapi.repository;

import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Game Repository.
 */
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByUser(User user);

    Optional<Game> findByUserAndName(final User user, final String name);

}
