package com.pidev.minesweeperapi.repository;

import com.pidev.minesweeperapi.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        User userToBeSaved = new User("someUser");
        User userSaved = userRepository.save(userToBeSaved);

        assertThat(userSaved).isNotNull();
        assertThat(userSaved.getId()).isNotNull();
        assertThat(userSaved.getName()).isEqualTo(userToBeSaved.getName());
    }

    @Test
    public void testFindByName() {
        User userToBeSaved = new User("username");
        User userSaved = userRepository.save(userToBeSaved);

        Optional<User> userOptional = userRepository.findByName(userSaved.getName());

        assertThat(userOptional.isPresent()).isTrue();
        User user = userOptional.get();
        assertThat(user.getName()).isEqualTo(userSaved.getName());
    }

    @Test
    public void testFindByNameNotFound() {
        Optional<User> userOptional = userRepository.findByName("unknown");
        assertThat(userOptional.isEmpty()).isTrue();
    }

    @Test
    public void testFindAll() {
        User userToBeSaved = new User("anUser");
        userRepository.save(userToBeSaved);

        userToBeSaved = new User("otherUser");
        userRepository.save(userToBeSaved);

        List<User> users = userRepository.findAll();
        assertThat(users.isEmpty()).isFalse();
        assertThat(users.size()).isEqualTo(2);
    }

    //@Test
    public void testFindAllEmptyList() {
        List<User> users = userRepository.findAll();
        assertThat(users.isEmpty()).isTrue();
    }


}
