package topg.bimber_user_service.controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.Booking;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.service.UserService;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserById(@PathVariable("id") String userId) {
        UserResponseDto message = userService.getUserById(userId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,message));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody  UserRequestDto dto){
            var response = userService.createUser(dto);
            return ResponseEntity.status(201).body(new BaseResponse<>(true,response));
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editUserById(@Valid @RequestBody UserAndAdminUpdateDto userAndAdminUpdateDto, @PathVariable("id") String userId) {
        UserResponseDto message = userService.editUserById(userAndAdminUpdateDto, userId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,message));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String userId) {
        String message = userService.deleteUserById(userId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,message));
    }

    @PatchMapping("/{userId}/fund")
    public ResponseEntity<?> fundAccount(
            @PathVariable String userId,
            @RequestParam("amount") BigDecimal amount) {
        String response = userService.fundAccount(userId, amount);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));
    }

    @GetMapping("/{userId}/details")
    public ResponseEntity<?> findById(@PathVariable String userId) {
        var response = userService.getUserById(userId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));
    }


    @PostMapping("/bookings")
    public ResponseEntity<?> bookRoom(@RequestBody BookingRequestDto bookingRequest) {
        Booking response = userService.bookRoom(bookingRequest);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam("userId") String userId) {
        String response = userService.cancelBooking(bookingId, userId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));

    }

    @PutMapping("/bookings/{bookingId}")
    public ResponseEntity<?> updateBooking(
            @PathVariable Long bookingId,
            @RequestBody BookingRequestDto updateRequest) {
        BookingResponseDto response = userService.updateBooking(bookingId, updateRequest);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> listAllBookings() {
        List<BookingResponseDto> bookings = userService.listAllBookings();
        return ResponseEntity.status(200).body(new BaseResponse<>(true,bookings));
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<?> getAllRooms() {
        List<RoomResponse> bookings = userService.getAllRooms();
        return ResponseEntity.status(200).body(new BaseResponse<>(true,bookings));
    }

    @GetMapping("/rooms/hotelId")
    public ResponseEntity<?> findAllRoomsByHotelId(@RequestBody Long hotelId) {
        List<NewRoomResponse> bookings = userService.findAllRoomsByHotelId(hotelId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,bookings));
    }



}


