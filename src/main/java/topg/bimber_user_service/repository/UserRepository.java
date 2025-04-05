package topg.bimber_user_service.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import topg.bimber_user_service.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u from User u where u.email=:email")
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
