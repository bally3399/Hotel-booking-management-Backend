package topg.bimber_user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.exceptions.UserNotFoundException;
import topg.bimber_user_service.models.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts ={"/db/data.sql"})
class UserServiceTest {
    @Autowired
    private UserService userService;
    private UserRequestDto dto;
    @BeforeEach
    public void setUp(){
        dto = new UserRequestDto();
        dto.setEmail("user@gmail.com");
        dto.setUsername("Username");
        dto.setPassword("password");
    }
    @Test
    void createUser() {
        var user = userService.createUser(dto);
        assertNotNull(user);
        assertThat(user.isSuccess()).isEqualTo(true);
    }

    @Test
    void getUserById() {
        var user = userService.getUserById("123e4567-e89b-12d3-a456-426614174001");
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("user1");
    }

    @Test
    void editUserById() {
        UserAndAdminUpdateDto dto = new UserAndAdminUpdateDto("newEmail","newUsername");
        UserResponseDto user  =  userService.editUserById(dto,"123e4567-e89b-12d3-a456-426614174001");
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("newUsername");
        assertThat(user.getEmail()).isEqualTo("newEmail");
    }

    @Test
    void deleteUserById() {
        var response =  userService.deleteUserById("123e4567-e89b-12d3-a456-426614174001");
        assertThrows(UserNotFoundException.class, ()->userService.deleteUserById("123e4567-e89b-12d3-a456-426614174001") );
    }

    @Test
    void fundAccount() {
        var response = userService.fundAccount("123e4567-e89b-12d3-a456-426614174001", BigDecimal.valueOf(500000));
        assertThat(userService.findById("123e4567-e89b-12d3-a456-426614174001").getBalance()).isEqualTo(BigDecimal.valueOf(900000).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void getNumberOfUsers() {
        var response = userService.getNumberOfUsers();
        assertThat(response.size()).isEqualTo(5);
    }
}