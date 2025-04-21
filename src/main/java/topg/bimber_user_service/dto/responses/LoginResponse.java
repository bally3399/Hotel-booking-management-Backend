package topg.bimber_user_service.dto.responses;

import lombok.*;
import topg.bimber_user_service.models.Role;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponse {
    private String message;
    private String jwtToken;
    private String refreshToken;
    private UserResponseDto user;



}