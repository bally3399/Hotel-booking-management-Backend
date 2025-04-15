package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    UserCreatedDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(String userId);
    UserResponseDto editUserById(UserAndAdminUpdateDto userAndAdminUpdateDto, String userId);
    String deleteUserById(String userId);
    String fundAccount(String userId, BigDecimal amount);
    User findById(String id);

    List<UserResponseDto> getNumberOfUsers();

    BookingResponseDto bookRoom(BookingRequestDto bookingRequest);

    String cancelBooking(Long bookingId, String userId);

    BookingResponseDto updateBooking(Long bookingId, BookingRequestDto updateRequest);

    List<BookingResponseDto> listAllBookings();
    List<NewRoomResponse> findAllRoomsByHotelId(Long hotelId);
    boolean isRoomAvailable(Long id);
    List<RoomResponse> getAllRooms ();
}
