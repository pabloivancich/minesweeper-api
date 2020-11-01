package com.pidev.minesweeperapi.controller;

import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.service.UserService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testFindAll() throws Exception {
        User user = new User("user");
        User otherUser = new User("otherUser");

        given(userService.findAll()).willReturn(List.of(user, otherUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFindByName() throws Exception {
        User user = new User("username");

        given(userService.findByName(anyString())).willReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("username"));
    }

    @Test
    public void testFindByNameNotFound() throws Exception {
        given(userService.findByName(anyString())).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testSave() throws Exception {
        User user = new User("name");

        mockMvc.perform( MockMvcRequestBuilders.post("/users/")
                .content(JsonUtil.OBJECT_MAPPER.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
