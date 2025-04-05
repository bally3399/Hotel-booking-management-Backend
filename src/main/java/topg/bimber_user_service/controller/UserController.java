package topg.bimber_user_service.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.responses.BookingResponseDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId) {
        UserResponseDto response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> editUserById(
            @PathVariable String userId,
            @RequestBody UserAndAdminUpdateDto userAndAdminUpdateDto) {
        UserResponseDto response = userService.editUserById(userAndAdminUpdateDto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable String userId) {
        String response = userService.deleteUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/fund")
    public ResponseEntity<String> fundAccount(
            @PathVariable String userId,
            @RequestParam("amount") BigDecimal amount) {
        String response = userService.fundAccount(userId, amount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/details")
    public ResponseEntity<User> findById(@PathVariable String userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/bookings")
    public ResponseEntity<BookingResponseDto> bookRoom(@RequestBody BookingRequestDto bookingRequest) {
        BookingResponseDto response = userService.bookRoom(bookingRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<String> cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam("userId") String userId) {
        String response = userService.cancelBooking(bookingId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/bookings/{bookingId}")
    public ResponseEntity<BookingResponseDto> updateBooking(
            @PathVariable Long bookingId,
            @RequestBody BookingRequestDto updateRequest) {
        BookingResponseDto response = userService.updateBooking(bookingId, updateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDto>> listAllBookings() {
        List<BookingResponseDto> bookings = userService.listAllBookings();
        return ResponseEntity.ok(bookings);
    }
}


