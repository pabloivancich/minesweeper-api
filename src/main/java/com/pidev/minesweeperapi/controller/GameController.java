package com.pidev.minesweeperapi.controller;

import com.pidev.minesweeperapi.controller.dto.CellActionRequest;
import com.pidev.minesweeperapi.controller.dto.CellActionResponse;
import com.pidev.minesweeperapi.exception.InvalidCellException;
import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.controller.dto.GameResponse;
import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(value="Games controller")
@RestController()
@RequestMapping("/minesweeper/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Retrieves a list of games for a given user.
     * @return a list of {@link Game}s
     */
    @ApiOperation(value = "Retrieves a list of games for a given user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of games retrieved successfully.")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<GameResponse>> findByUser(
            @RequestParam final long userId
    ) {
        User user = new User(userId);
        List<Game> games = gameService.findByUser(user);
        List<GameResponse> response = games.stream().map(
                GameResponse::createResponse
        ).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the user with the given name.
     * @param name the user name.
     * @return the user.
     */
    @ApiOperation(value = "Load the game with the given name for the given user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game retrieved successfully."),
            @ApiResponse(code = 404, message = "Game not found.")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GameResponse> findByUserAndName(
            @RequestParam final long userId,
            @PathVariable("name") final String name
    ) {
        User user = new User(userId);
        Optional<Game> game = gameService.findByUserAndName(user, name);

        return game.map(g -> {
            GameResponse response = GameResponse.createResponse(g);
            return ResponseEntity.ok().body(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new game.
     * @param game the game to be created.
     */
    @ApiOperation(value = "Creates a new game.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Game created successfully.")
    })
    @RequestMapping(
            method = RequestMethod.POST,
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> create(
            @RequestBody final Game game
    ) {
        gameService.create(game);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Saves the current game for a given user..
     */
    @ApiOperation(value = "Saves the current game for a given user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game saved successfully.")
    })
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> save(
            @RequestParam final Long userId
    ) {
        gameService.save(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Executes an action on a given cell in the current game for an user.
     * @return a response with the information about the execution.
     */
    @ApiOperation(value = "Executes an action on a given cell.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cell action executed successfully.")
    })
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/cells",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CellActionResponse> executeCellAction(
            @RequestParam final long userId,
            @RequestBody final CellActionRequest request
    ) throws InvalidCellException {
        CellActionResponse response = gameService.executeCellAction(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
