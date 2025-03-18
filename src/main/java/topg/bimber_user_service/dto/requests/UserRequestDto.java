package topg.bimber_user_service.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 6, message = "Username must be more than 5 characters")
        private String username;

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be more than 5 characters")
        private String password;

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        private String email;
}
