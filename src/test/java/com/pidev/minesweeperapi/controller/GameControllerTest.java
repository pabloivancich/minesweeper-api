package com.pidev.minesweeperapi.controller;

import com.pidev.minesweeperapi.model.Game;
import com.pidev.minesweeperapi.model.GameDifficulty;
import com.pidev.minesweeperapi.model.GameMap;
import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.service.GameService;
import com.pidev.minesweeperapi.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void testFindByUser() throws Exception {
        User user = new User(1);
        Game game = new Game();

        given(gameService.findByUser(user)).willReturn(List.of(game));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/")
                .param("userId", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFindByUserAndName() throws Exception {
        User user = new User(1);
        Game game = new Game(
                "gamename",
                GameDifficulty.BEGINNER,
                user,
                new GameMap()
        );

        given(gameService.findByUserAndName(anyObject(), anyString())).willReturn(Optional.of(game));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/gamename")
                .param("userId", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("gamename"));
    }

    @Test
    public void testFindByUserAndNameNotFound() throws Exception {
        given(gameService.findByUserAndName(anyObject(), anyString())).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/games/gamename")
                .param("userId", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        User user = new User(1);
        Game game = new Game(
                "gamename",
                GameDifficulty.BEGINNER,
                user,
                new GameMap()
        );

        mockMvc.perform( MockMvcRequestBuilders.post("/games/")
                .content(JsonUtil.OBJECT_MAPPER.writeValueAsString(game))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testSave() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.put("/games/current")
                .param("userId", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
