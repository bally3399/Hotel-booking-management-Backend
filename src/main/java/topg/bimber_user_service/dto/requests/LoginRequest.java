package topg.bimber_user_service.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @NotEmpty(message = "Username must not be empty")
    private String email;

    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password must not be empty")
    private String password;
}
