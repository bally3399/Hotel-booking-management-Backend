package topg.bimber_user_service.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import topg.bimber_user_service.models.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByOwnerEmail(String lowerCase);
}
