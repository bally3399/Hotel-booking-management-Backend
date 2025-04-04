package topg.bimber_user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.LoginRequest;
import topg.bimber_user_service.dto.responses.JwtResponseDto;
import topg.bimber_user_service.dto.requests.LoginRequestDto;
import topg.bimber_user_service.dto.responses.LoginResponse;
import topg.bimber_user_service.dto.responses.UserCreatedDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.exceptions.InvalidDetailsException;
import topg.bimber_user_service.service.AdminServiceImpl;
import topg.bimber_user_service.service.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AdminServiceImpl adminServiceImpl;
    private final UserServiceImpl userServiceImpl;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = adminServiceImpl.login(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch(InvalidDetailsException e){
            return ResponseEntity.status(HttpStatus.valueOf(e.getMessage())).body(e.getMessage());
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> loginAdmin(@RequestBody @Valid LoginRequest request)  {
        var result = adminServiceImpl.login(request);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/user/register")
    public ResponseEntity<UserCreatedDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserCreatedDto message = userServiceImpl.createUser(userRequestDto);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<UserCreatedDto> createAdmin(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserCreatedDto message = adminServiceImpl.createAdmin(userRequestDto);
        return ResponseEntity.ok(message);
    }


}
