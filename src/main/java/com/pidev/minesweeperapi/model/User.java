package com.pidev.minesweeperapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class that represents a user of the game.
 */
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    /**
     * Default constructor. For Hibernate.
     */
    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
