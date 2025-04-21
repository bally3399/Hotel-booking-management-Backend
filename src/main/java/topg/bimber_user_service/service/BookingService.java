package topg.bimber_user_service.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.responses.BookingResponseDto;
import topg.bimber_user_service.models.Booking;

import java.util.List;

public interface BookingService {

    Booking bookRoom(BookingRequestDto bookingRequestDto);

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    void updateExpiredBookings();

    String cancelBooking(Long bookingId, String userId);

    BookingResponseDto updateBooking(Long bookingId, BookingRequestDto bookingRequestDto);
    BookingResponseDto findBookingByRoomId(Long roomId);
    List<BookingResponseDto> listAllBookings();
    List<BookingResponseDto> getUserBookings(String UserId);

}
