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
import topg.bimber_user_service.models.Admin;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.repository.AdminRepository;
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
    private final AdminRepository adminRepository;
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
        String email = authResult.getName(); // Extract the email from the Authentication object

        // Try to find the user in the UserRepository
        Optional<User> optionalUser = userRepository.findByUsername(email);

        if (optionalUser.isPresent()) {
            handleSuccessfulLogin(response, token, optionalUser.get());
            return;
        }

        // If not found in UserRepository, try AdminRepository
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(email);

        if (optionalAdmin.isPresent()) {
            handleSuccessfulLogin(response, token, optionalAdmin.get());
            return;
        }

        // If neither User nor Admin is found, throw an exception
        throw new RuntimeException("User or Admin not found for email: " + email);
    }

    private void handleSuccessfulLogin(HttpServletResponse response, String token, Object entity) throws IOException {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(token);
        loginResponse.setMessage("Successful Authentication");

        // Map the entity (User or Admin) to UserResponseDto
        UserResponseDto userResponseDto = mapper.map(entity, UserResponseDto.class);
        loginResponse.setUser(userResponseDto);

        // Wrap the LoginResponse in a BaseResponse
        BaseResponse<LoginResponse> authResponse = new BaseResponse<>(true, loginResponse);
        authResponse.setData(loginResponse);

        // Write the response as JSON
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        response.getOutputStream().write(objectMapper.writeValueAsBytes(authResponse));
        response.getOutputStream().flush();
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