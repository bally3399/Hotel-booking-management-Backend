package topg.bimber_user_service.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.UserCreatedDto;
import topg.bimber_user_service.service.UserService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceTest {
    private UserService userService;

    @Test
    public void createUserTest() {
        UserRequestDto createUser = new UserRequestDto();
        createUser.setUsername("username");
        createUser.setPassword("password");
        createUser.setEmail("user@gmail.com");
        UserCreatedDto createdUser = userService.createUser(createUser);
        assertThat(createdUser.getMessage()).isEqualTo("Registration successful");
    }
}
