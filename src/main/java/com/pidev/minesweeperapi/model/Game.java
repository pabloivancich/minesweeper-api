package com.pidev.minesweeperapi.model;

import org.hibernate.annotations.Type;

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

    @Column(name = "name", nullable = false, unique = true)
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

    @Column(name = "rows")
    private Integer rows;

    @Column(name = "columns")
    private Integer columns;

    @Column(name = "mines")
    private Integer mines;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name = "map")
    @Type(type = "com.pidev.minesweeperapi.config.hibernate.GameMapType")
    private GameMap map;

    /**
     * Default constructor. For Jackson and Hibernate.
     */
    public Game() {

    }

    public Game(
            final String name,
            final long timePlayed,
            final int minesRevealed,
            final GameState state,
            final GameDifficulty difficulty,
            final User user,
            final GameMap map
    ) {
        this.name = name;
        this.timePlayed = timePlayed;
        this.minesRevealed = minesRevealed;
        this.state = state;
        this.difficulty = difficulty;
        this.moves = 0;
        this.user = user;
        this.map = map;
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

    public int getMoves() {
        return moves;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public Integer getMines() {
        return mines;
    }

    public User getUser() {
        return user;
    }

    public GameMap getMap() {
        return map;
    }
}
