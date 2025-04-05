package topg.bimber_user_service.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import topg.bimber_user_service.dto.requests.LoginRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/db/data.sql"})
class AuthServiceTest {


    @Test
    void login() {
//        LoginRequest request = new LoginRequest();
//        request.setEmail("user1@example.com");
//        request.setPassword("Password1@");
//        var response = authService.login(request);
//        assertNotNull(response);
//        System.out.println(response.getJwtToken());
    }
}