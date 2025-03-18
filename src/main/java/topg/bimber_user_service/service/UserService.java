package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.responses.UserCreatedDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;

import java.math.BigDecimal;

public interface UserService {
    UserCreatedDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(String userId);
    UserResponseDto editUserById(UserAndAdminUpdateDto userAndAdminUpdateDto, String userId);
    String deleteUserById(String userId);
    String fundAccount(String userId, BigDecimal amount);

    Long getNumberOfUsers();
}
