package com.pidev.minesweeperapi.model;

/**
 * Represents a response with the game information.
 */
public class GameResponse {

    private String name;

    private long timePlayed;

    private int moves;

    private int redFlags;

    private String state;

    private String difficulty;

    private Integer rows;

    private Integer columns;

    private Integer mines;

    private String username;

    private Long userId;

    /**
     * Default constructor. For Jackson
     */
    public GameResponse() {

    }

    public static GameResponse createResponse(Game game) {
        GameResponse response = new GameResponse();
        response.name = game.getName();
        response.timePlayed = game.getTimePlayed();
        response.moves = game.getMoves();
        response.redFlags = game.getRedFlags();
        response.state = game.getState().state();
        response.difficulty = game.getDifficulty().getDifficulty();
        response.rows = game.getMap().getRows();
        response.columns = game.getMap().getColumns();
        response.mines = game.getMap().getMines();
        response.userId = game.getUser().getId();
        response.username = game.getUser().getName();

        return response;
    }

    public String getName() {
        return name;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public int getMoves() {
        return moves;
    }

    public int getRedFlags() {
        return redFlags;
    }

    public String getState() {
        return state;
    }

    public String getDifficulty() {
        return difficulty;
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

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }
}
