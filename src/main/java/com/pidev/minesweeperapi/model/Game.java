package com.pidev.minesweeperapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents a minesweeper game.
 */
@Entity(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "time_played")
    private long timePlayed;

    @Column(name = "moves")
    private int moves;

    @Column(name = "mines_revealed")
    private int minesRevealed;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private GameState state;

    @Column(name = "difficulty")
    @Enumerated(EnumType.STRING)
    private GameDifficulty difficulty;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    /**
     * Default constructor. For Hibernate.
     */
    public Game() {

    }

    public Game(
            final String name,
            final long timePlayed,
            final int minesRevealed,
            final GameState state,
            final GameDifficulty difficulty,
            final User user
    ) {
        this.name = name;
        this.timePlayed = timePlayed;
        this.minesRevealed = minesRevealed;
        this.state = state;
        this.difficulty = difficulty;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public int getMinesRevealed() {
        return minesRevealed;
    }

    public GameState getState() {
        return state;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public User getUser() {
        return user;
    }
}
