package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.LoginRequest;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.responses.LoginResponse;
import topg.bimber_user_service.dto.responses.UserCreatedDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;

public interface AdminService {
    UserCreatedDto createAdmin(UserRequestDto userRequestDto);

    LoginResponse login(LoginRequest loginRequest);

    UserResponseDto getAdminById(String adminId);
//    UserResponseDto editAdminById(UserAndAdminUpdateDto adminUpdateRequestDto, String adminId);
    String deleteAdminById(String adminId);

    void deleteAll();
}
