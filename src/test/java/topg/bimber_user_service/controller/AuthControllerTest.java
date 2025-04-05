package topg.bimber_user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.Test;
import org.mockito.plugins.MockMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import topg.bimber_user_service.dto.requests.LoginRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mock;
    @Test
    @Sql(scripts = {"/db/data.sql"})
    void loginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user2@example.com");
        loginRequest.setPassword("Password1@");
        ObjectMapper objectMapper = new ObjectMapper();
        mock.perform(post("/api/v1/auth")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print());

    }
}