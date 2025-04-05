package topg.bimber_user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.responses.BaseResponse;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;

import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserById(@PathVariable("id") String userId) {
        UserResponseDto message = userServiceImpl.getUserById(userId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,message));
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> editUserById(@Valid @RequestBody UserAndAdminUpdateDto userAndAdminUpdateDto, @PathVariable("id") String userId) {
        UserResponseDto message = userServiceImpl.editUserById(userAndAdminUpdateDto, userId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String userId) {
//        if (!user.isEnabled()) {
//            throw new IllegalStateException("Your account is not activated. Please activate your account.");
//
//        }
        String message = userServiceImpl.deleteUserById(userId);
        return ResponseEntity.ok(message);
    }

//    @GetMapping("accountVerification/{token}")
//    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
//        try {
//            userServiceImpl.verifyToken(token);  // Assuming verifyToken checks the token validity and does the necessary action
//            return new ResponseEntity<>("Account created successfully", OK);
//
//        } catch (Exception e) {
//            // Handle errors like invalid token
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
//        }
//    }


    @PostMapping("/{userId}/fund")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> fundAccount(
            @PathVariable String userId,
            @RequestParam BigDecimal amount
    ) {
        try {
            userServiceImpl.fundAccount(userId, amount);
            return ResponseEntity.ok("Account funded successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred.");
        }
    }
}