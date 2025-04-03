package topg.bimber_user_service.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.responses.BookingResponseDto;

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
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    public void setUp(){
        bookingRequestDto = new BookingRequestDto(
                "123e4567-e89b-12d3-a456-426614174001",
                1L,
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                true
        );
    }

    @Test
    void bookRoom() {
        var response = bookingService.bookRoom(bookingRequestDto);
        assertNotNull(response);
        log.info("Data:  ", response);
    }

    @Test
    void cancelBooking() {
        var response = bookingService.cancelBooking(10L,"123e4567-e89b-12d3-a456-426614174001");
        assertThrows(IllegalStateException.class, ()->bookingService.cancelBooking(10L,"123e4567-e89b-12d3-a456-426614174001"));
    }

    @Test
    void updateBooking() {
        BookingRequestDto dto = BookingRequestDto.builder()
                .startDate(LocalDateTime.now().plusDays(3))
                .roomId(1L)
                .build();
        var response = bookingService.updateBooking(10L,dto);
        assertThat(dto.getStartDate().toLocalDate()).isEqualTo(LocalDateTime.now().plusDays(3).toLocalDate());
    }

    @Test
    void listAllBookings() {
        List<BookingResponseDto> response = bookingService.listAllBookings();
        assertThat(response.size()).isEqualTo(3);
    }
}