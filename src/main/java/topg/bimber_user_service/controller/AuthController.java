package topg.bimber_user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.LoginRequest;
import topg.bimber_user_service.dto.responses.BaseResponse;
import topg.bimber_user_service.dto.responses.LoginResponse;
import topg.bimber_user_service.dto.responses.UserCreatedDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.service.AdminServiceImpl;
import topg.bimber_user_service.service.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AdminServiceImpl adminServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/user/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserCreatedDto message = userServiceImpl.createUser(userRequestDto);
        return ResponseEntity.status(201).body(new BaseResponse<>(true,message));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserCreatedDto message = adminServiceImpl.createAdmin(userRequestDto);
        return ResponseEntity.status(201).body(new BaseResponse<>(true,message));
    }



}
