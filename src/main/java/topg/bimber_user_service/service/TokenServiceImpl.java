package topg.bimber_user_service.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import topg.bimber_user_service.exceptions.UserNotFoundException;
import topg.bimber_user_service.models.Token;
import topg.bimber_user_service.repository.TokenRepository;

import java.security.SecureRandom;


@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;
    @Override
    public String createToken(String email) {
        String token = generateToken();
        Token userToken = new Token();
        userToken.setToken(token);
        userToken.setOwnerEmail(email.toLowerCase());
        Token savedToken = tokenRepository.save(userToken);
        return savedToken.getToken();
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        for (int count = 0; count < 5; count++) {
            SecureRandom secureRandom = new SecureRandom();
            int numbers = secureRandom.nextInt(1, 9);
            token.append(numbers);
        }
        return String.valueOf(token);
    }

    @Override
    public Token findByUserEmail(String email) {
        return tokenRepository.findByOwnerEmail(email.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("Provide valid mail"));
    }

    @Override
    public void deleteToken(String id) {
        tokenRepository.deleteById(id);
    }

}
