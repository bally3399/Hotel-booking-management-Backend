package topg.bimber_user_service.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.models.Booking;
import topg.bimber_user_service.models.BookingStatus;
import topg.bimber_user_service.models.Room;
import topg.bimber_user_service.models.RoomType;
import topg.bimber_user_service.repository.BookingRepository;
import topg.bimber_user_service.repository.RoomRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/db/data.sql"})
@Slf4j
class BookingServiceTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    public void setUp() {
        // Mock authenticated user
        UserDetails userDetails = new User("user1", "password", List.of());
        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                )
        );

        // Setup BookingRequestDto
        bookingRequestDto = BookingRequestDto.builder()
                .roomType(RoomType.DELUXE)
                .hotelName("Grand Royale")
                .startDate(LocalDateTime.now().plusDays(10))
                .endDate(LocalDateTime.now().plusDays(12))
                .build();
    }

    @Test
    void bookRoom_success() {
        // Act
        Booking response = bookingService.bookRoom(bookingRequestDto);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("user1", response.getUser().getUsername());
        assertEquals(RoomType.DELUXE, response.getRoom().getRoomType());
        assertEquals("Grand Royale", response.getHotel().getName());
        assertEquals(bookingRequestDto.getStartDate(), response.getStartDate());
        assertEquals(bookingRequestDto.getEndDate(), response.getEndDate());
        assertEquals(BookingStatus.ACTIVE, response.getStatus());
        assertFalse(response.isPaid());
        assertEquals(new BigDecimal("100000"), response.getTotalPrice()); // 50000 × 2 days

        // Verify room availability
        Room room = roomRepository.findById(response.getRoom().getId()).orElseThrow();
        assertFalse(room.isAvailable());

        log.info("Booked: {}", response);
    }

    @Test
    void bookRoom_noAvailableRooms_throwsException() {
        // Arrange: Request a room type with no available rooms (e.g., all DELUXE rooms booked)
        bookingRequestDto = BookingRequestDto.builder()
                .roomType(RoomType.DELUXE)
                .hotelName("Grand Royale")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(5))
                .build();

        // Book all DELUXE rooms
        Booking firstBooking = bookingService.bookRoom(bookingRequestDto);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.bookRoom(bookingRequestDto)
        );
        assertEquals("No available rooms of type DELUXE for the selected dates", exception.getMessage());

        // Cleanup
        bookingRepository.delete(firstBooking);
        Room room = roomRepository.findById(firstBooking.getRoom().getId()).orElseThrow();
        room.setAvailable(true);
        roomRepository.save(room);
    }

    @Test
    void bookRoom_invalidDates_throwsException() {
        // Arrange: Invalid dates (startDate > endDate)
        bookingRequestDto = BookingRequestDto.builder()
                .roomType(RoomType.DELUXE)
                .hotelName("Grand Royale")
                .startDate(LocalDateTime.now().plusDays(5))
                .endDate(LocalDateTime.now().plusDays(3))
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.bookRoom(bookingRequestDto)
        );
        assertEquals("Start date must be before end date", exception.getMessage());
    }

    @Test
    void updateExpiredBookings() {
        // Arrange: Create an expired booking
        bookingRequestDto = BookingRequestDto.builder()
                .roomType(RoomType.SINGLE)
                .hotelName("Grand Royale")
                .startDate(LocalDateTime.now().minusDays(5))
                .endDate(LocalDateTime.now().minusDays(1))
                .build();

        Booking booking = bookingService.bookRoom(bookingRequestDto);
        Long bookingId = booking.getId();
        Long roomId = booking.getRoom().getId();

        // Act
        bookingService.updateExpiredBookings();

        // Assert
        Booking updatedBooking = bookingRepository.findById(bookingId).orElseThrow();
        assertEquals(BookingStatus.EXPIRED, updatedBooking.getStatus());

        Room room = roomRepository.findById(roomId).orElseThrow();
        assertTrue(room.isAvailable());

        log.info("Expired booking: {}", updatedBooking);
    }
}