package topg.bimber_user_service.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import topg.bimber_user_service.dto.requests.LoginRequest;
import topg.bimber_user_service.dto.responses.BaseResponse;
import topg.bimber_user_service.dto.responses.LoginResponse;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            InputStream requestBody = request.getInputStream();
            LoginRequest loginRequest = objectMapper.readValue(requestBody, LoginRequest.class);
            Authentication authenticationResult = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            return authenticationResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = generateAccessToken(authResult);

        String email = authResult.getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setJwtToken(token);
            loginResponse.setMessage("Successful Authentication");
           loginResponse.setUser(mapper.map(user, UserResponseDto.class));

            // Wrap the LoginResponse in a BaseResponse
            BaseResponse<LoginResponse> authResponse = new BaseResponse<>(true, loginResponse);
            authResponse.setData(loginResponse);

            // Write the response as JSON
            response.setContentType("application/json");
            response.setStatus(HttpStatus.OK.value());
            response.getOutputStream().write(objectMapper.writeValueAsBytes(authResponse));
            response.getOutputStream().flush();
        } else {
            throw new RuntimeException("User not found for email: " + email);
        }
    }

    private static String generateAccessToken(Authentication authResult) {
        return JWT.create()
                .withIssuer("mavericks_hub")
                .withArrayClaim("roles", getClaimsFrom(authResult.getAuthorities()))
                .withExpiresAt(Instant.now().plusSeconds(24 * 60 * 60))
                .sign(Algorithm.HMAC512("secret"));
    }

    private static String[] getClaimsFrom(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage(exception.getMessage());
        BaseResponse<LoginResponse> baseResponse = new BaseResponse<>(false, loginResponse); // Set success to false for failure
        baseResponse.setData(loginResponse);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream().write(objectMapper.writeValueAsBytes(baseResponse));
        response.getOutputStream().flush();
    }
}