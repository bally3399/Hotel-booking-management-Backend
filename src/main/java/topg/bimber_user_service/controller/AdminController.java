package topg.bimber_user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.service.AdminServiceImpl;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;


    @GetMapping("/me/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> getAdminById(@PathVariable("id") String userId) {
        UserResponseDto message = adminServiceImpl.getAdminById(userId);
        return ResponseEntity.ok(message);
    }

//    @PutMapping("/me/edit/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<UserResponseDto> editUserById(@Valid @RequestBody UserAndAdminUpdateDto adminUpdateRequestDto, @PathVariable("id") String userId) {
//        UserResponseDto message = adminServiceImpl.editAdminById(adminUpdateRequestDto, userId);
//        return ResponseEntity.ok(message);
//    }
//
//    @DeleteMapping("/me/delete/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> deleteAdminById(@PathVariable("id") String userId) {
//        String message = adminServiceImpl.deleteAdminById(userId);
//        return ResponseEntity.ok(message);
//    }
//
//    @GetMapping("accountVerification/{token}")
//    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
//        try {
//            adminServiceImpl.verifyToken(token);  // Assuming verifyToken checks the token validity and does the necessary action
//            return new ResponseEntity<>("Account created successfully", OK);
//        } catch (Exception e) {
//            // Handle errors like invalid token
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
//        }
//    }

}