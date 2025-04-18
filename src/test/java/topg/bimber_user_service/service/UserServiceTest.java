
package topg.bimber_user_service.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.BookingResponseDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.exceptions.UserNotFoundException;
import topg.bimber_user_service.models.Booking;
import topg.bimber_user_service.models.BookingStatus;
import topg.bimber_user_service.models.RoomType;
import topg.bimber_user_service.repository.BookingRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = "/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingRepository bookingRepository;

    private UserRequestDto dto;

    @BeforeEach
    public void setUp() {
        dto = new UserRequestDto();
        dto.setEmail("user@gmail.com");
        dto.setUsername("Username");
        dto.setPassword("password");
    }

    @Test
    void createUser() {
        var user = userService.createUser(dto);
        assertNotNull(user);
        assertThat(user.getEmail()).isEqualTo("user@gmail.com");
        assertThat(user.getUsername()).isEqualTo("Username");
    }

    @Test
    void getUserById() {
        var user = userService.getUserById("123e4567-e89b-12d3-a456-426614174001");
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("user1");
        assertThat(user.getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    void editUserById() {
        UserAndAdminUpdateDto dto = new UserAndAdminUpdateDto("newEmail@example.com", "newUsername");
        UserResponseDto user = userService.editUserById(dto, "123e4567-e89b-12d3-a456-426614174001");
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("newUsername");
        assertThat(user.getEmail()).isEqualTo("newEmail@example.com");
    }

    @Test
    @Transactional
    void deleteUserById() {
        var response = userService.deleteUserById("123e4567-e89b-12d3-a456-426614174001");
        assertNotNull(response);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById("123e4567-e89b-12d3-a456-426614174001"));
    }

    @Test
    void fundAccount() {
        var response = userService.fundAccount("123e4567-e89b-12d3-a456-426614174001", BigDecimal.valueOf(500000));
        assertThat(userService.findById("123e4567-e89b-12d3-a456-426614174001").getBalance())
                .isEqualTo(BigDecimal.valueOf(900000).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void getNumberOfUsers() {
        var response = userService.getNumberOfUsers();
        assertThat(response.size()).isEqualTo(5);
    }

//    @Test
//    @Transactional
//    @WithMockUser(username = "user1")
//    public void testThatUserCanBookRoom() {
//        BookingRequestDto bookingRequest = BookingRequestDto.builder()
//                .roomType(RoomType.DELUXE)
//                .hotelName("Grand Royale")
//                .startDate(LocalDateTime.now().plusDays(10))
//                .endDate(LocalDateTime.now().plusDays(12))
//                .build();
//
//        Booking response = userService.bookRoom(bookingRequest);
//
//        assertNotNull(response);
//        assertThat(response.getRoom().getRoomType()).isEqualTo(RoomType.DELUXE);
//        assertThat(response.getHotel().getName()).isEqualTo("Grand Royale");
//        assertThat(response.getStatus()).isEqualTo(BookingStatus.ACTIVE);
//        assertThat(response.getTotalPrice()).isEqualTo(BigDecimal.valueOf(100000).setScale(2, RoundingMode.HALF_UP)); // 2 days * 50000
//    }

//package topg.bimber_user_service.service;
//
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import topg.bimber_user_service.dto.requests.BookingRequestDto;
//import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
//import topg.bimber_user_service.dto.requests.UserRequestDto;
//import topg.bimber_user_service.dto.responses.BookingResponseDto;
//import topg.bimber_user_service.dto.responses.UserResponseDto;
//import topg.bimber_user_service.exceptions.UserNotFoundException;
//import topg.bimber_user_service.models.Booking;
//import topg.bimber_user_service.models.BookingStatus;
//import topg.bimber_user_service.models.RoomType;
//import topg.bimber_user_service.repository.BookingRepository;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//@Sql(scripts = "/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BookingRepository bookingRepository;
//
//    private UserRequestDto dto;
//
//    @BeforeEach
//    public void setUp() {
//        dto = new UserRequestDto();
//        dto.setEmail("user@gmail.com");
//        dto.setUsername("Username");
//        dto.setPassword("password");
//    }
//

//    @Test
//    void createUser() {
//        var user = userService.createUser(dto);
//        assertNotNull(user);
//        assertThat(user.getEmail()).isEqualTo("user@gmail.com");
//        assertThat(user.getUsername()).isEqualTo("Username");
//    }
//
//    @Test
//    void getUserById() {
//        var user = userService.getUserById("123e4567-e89b-12d3-a456-426614174001");
//        assertNotNull(user);
//        assertThat(user.getUsername()).isEqualTo("user1");
//        assertThat(user.getEmail()).isEqualTo("user1@example.com");
//    }
//
//    @Test
//    void editUserById() {
//        UserAndAdminUpdateDto dto = new UserAndAdminUpdateDto("newEmail@example.com", "newUsername");
//        UserResponseDto user = userService.editUserById(dto, "123e4567-e89b-12d3-a456-426614174001");
//        assertNotNull(user);
//        assertThat(user.getUsername()).isEqualTo("newUsername");
//        assertThat(user.getEmail()).isEqualTo("newEmail@example.com");
//    }
//
//    @Test
//    @Transactional
//    void deleteUserById() {
//        var response = userService.deleteUserById("123e4567-e89b-12d3-a456-426614174001");
//        assertNotNull(response);
//        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById("123e4567-e89b-12d3-a456-426614174001"));
//    }
//
//    @Test
//    void fundAccount() {
//        var response = userService.fundAccount("123e4567-e89b-12d3-a456-426614174001", BigDecimal.valueOf(500000));
//        assertThat(userService.findById("123e4567-e89b-12d3-a456-426614174001").getBalance())
//                .isEqualTo(BigDecimal.valueOf(900000).setScale(2, RoundingMode.HALF_UP));
//    }
//
//    @Test
//    void getNumberOfUsers() {
//        var response = userService.getNumberOfUsers();
//        assertThat(response.size()).isEqualTo(5);
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "user1")
//    public void testThatUserCanBookRoom() {
//        BookingRequestDto bookingRequest = BookingRequestDto.builder()
//                .roomType(RoomType.DELUXE)
//                .hotelName("Grand Royale")
//                .startDate(LocalDateTime.now().plusDays(10))
//
//                .endDate(LocalDateTime.now().plusDays(12))
//                .build();
//
//        Booking response = userService.bookRoom(bookingRequest);
//
//        assertNotNull(response);
//        assertThat(response.getRoom().getRoomType()).isEqualTo(RoomType.DELUXE);
//        assertThat(response.getHotel().getName()).isEqualTo("Grand Royale");
//        assertThat(response.getStatus()).isEqualTo(BookingStatus.ACTIVE);
//        assertThat(response.getTotalPrice()).isEqualTo(BigDecimal.valueOf(100000).setScale(2, RoundingMode.HALF_UP)); // 2 days * 50000
//    }
//
////    @Test
////    @Transactional
////    @WithMockUser(username = "user1")
////    public void testThatUserCanCancelBooking() {
////        Long bookingId = 10L;
////        String userId = "123e4567-e89b-12d3-a456-426614174001";
////
////        String response = userService.cancelBooking(bookingId, userId);
////
////        assertNotNull(response);
////        assertEquals("Booking with ID 10 has been cancelled successfully. Refund processed.", response);
////        assertTrue(bookingRepository.findById(bookingId)
////                .map(booking -> BookingStatus.CANCELLED.equals(booking.getStatus()))
////                .orElse(false));
////    }
//
////    @Test
////    @Transactional
////    @WithMockUser(username = "user2")
////    public void testThatUserCanUpdateBooking() {
////        Long bookingId = 11L;
////        BookingRequestDto updateRequest = BookingRequestDto.builder()
////                .roomType(RoomType.SINGLE)
////                .hotelName("Grand Royale")
////                .startDate(LocalDateTime.now().plusDays(5))
////                .endDate(LocalDateTime.now().plusDays(7))
////                .build();
////
////        BookingResponseDto response = userService.updateBooking(bookingId, updateRequest);
////
////        assertNotNull(response);
////        assertThat(response.getRoomType()).isEqualTo(RoomType.SINGLE);
////        assertThat(response.getHotelName()).isEqualTo("Grand Royale");
////        assertThat(response.getStartDate()).isEqualToIgnoringNanos(updateRequest.getStartDate());
////        assertThat(response.getEndDate()).isEqualToIgnoringNanos(updateRequest.getEndDate());
////        assertThat(response.getTotalPrice()).isEqualTo(BigDecimal.valueOf(70000).setScale(2, RoundingMode.HALF_UP)); // 2 days * 35000
////    }
//
//    @Test
//    public void testThatUserCanViewAllBooking() {
//        List<BookingResponseDto> bookings = userService.listAllBookings();
//
//        assertNotNull(bookings);
//        assertEquals(3, bookings.size());
//    }
//}