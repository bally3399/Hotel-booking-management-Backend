package topg.bimber_user_service.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.BookingResponseDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.exceptions.UserNotFoundException;
import topg.bimber_user_service.repository.BookingRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static topg.bimber_user_service.models.BookingStatus.CANCELLED;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = "/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceTest {
    @Autowired
    private UserService userService;
    private UserRequestDto dto;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp(){
        dto = new UserRequestDto();
        dto.setEmail("user@gmail.com");
        dto.setUsername("Username");
        dto.setPassword("password");
    }
    @Test
    void createUser() {
        var user = userService.createUser(dto);
        assertNotNull(user);
        assertThat(user.isSuccess()).isEqualTo(true);
    }

    @Test
    void getUserById() {
        var user = userService.getUserById("123e4567-e89b-12d3-a456-426614174001");
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("user1");
    }

    @Test
    void editUserById() {
        UserAndAdminUpdateDto dto = new UserAndAdminUpdateDto("newEmail","newUsername");
        UserResponseDto user  =  userService.editUserById(dto,"123e4567-e89b-12d3-a456-426614174001");
        assertNotNull(user);
        assertThat(user.getUsername()).isEqualTo("newUsername");
        assertThat(user.getEmail()).isEqualTo("newEmail");
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
        assertThat(userService.findById("123e4567-e89b-12d3-a456-426614174001").getBalance()).isEqualTo(BigDecimal.valueOf(900000).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void getNumberOfUsers() {
        var response = userService.getNumberOfUsers();
        assertThat(response.size()).isEqualTo(5);
    }

    @Test
    @Transactional
    public void testThatUserCanBookRoom() {
        BookingRequestDto bookingRequest = BookingRequestDto.builder()
                .userId("123e4567-e89b-12d3-a456-426614174001")
                .roomId(1L)
                .hotelId(1L)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(3))
                .isPaid(false)
                .build();

        BookingResponseDto response = userService.bookRoom(bookingRequest);
        assertNotNull(response);
    }

    @Test
    @Transactional
    public void testThatUserCanCancelBooking() {
        Long bookingId = 10L;
        String userId = "123e4567-e89b-12d3-a456-426614174001";

        String response = userService.cancelBooking(bookingId, userId);

        assertNotNull(response);
        assertEquals("Booking with ID 10 has been cancelled successfully. Refund processed.", response);
        assertTrue(bookingRepository.findById(bookingId).isEmpty() ||
                CANCELLED.equals(bookingRepository.findById(bookingId).get().getStatus()));
    }

    @Test
    @Transactional
    public void testThatUserCanUpdateBooking() {
        Long bookingId = 11L;
        BookingRequestDto updateRequest = BookingRequestDto.builder()
                .userId("123e4567-e89b-12d3-a456-426614174002") // user2
                .roomId(2L)
                .hotelId(1L)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(3))
                .isPaid(true)
                .build();

        BookingResponseDto response = userService.updateBooking(bookingId, updateRequest);

        assertNotNull(response);

    }

    @Test
    public void testThatUserCanViewAllBooking() {
        List<BookingResponseDto> bookings = userService.listAllBookings();

        assertNotNull(bookings);
        assertEquals(3, bookings.size());

    }
}