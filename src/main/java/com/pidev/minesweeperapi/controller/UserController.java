package com.pidev.minesweeperapi.controller;

import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.service.UserService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(value="User controller")
@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list with all the users.
     * @return a list of {@link User}s
     */
    @ApiOperation(value = "Retrieves a list with all the users.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of users retrieved successfully.")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves the user with the given name.
     * @param name the user name.
     * @return the user.
     */
    @ApiOperation(value = "Retrieves the user with the given name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User retrieved successfully."),
            @ApiResponse(code = 404, message = "User not found.")
    })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> findByName(@PathVariable("name") final String name) {
        Optional<User> user = userService.findByName(name);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     * @param user the user to be created.
     * @return the user created.
     */
    @ApiOperation(value = "Creates a new user.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully.")
    })
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> create(@RequestBody final User user) {
        User userSaved = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

}
