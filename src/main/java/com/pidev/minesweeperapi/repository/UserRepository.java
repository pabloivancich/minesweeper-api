package com.pidev.minesweeperapi.repository;

import com.pidev.minesweeperapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository.
 * User Repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(final String name);

}