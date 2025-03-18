package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.responses.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto bookRoom(BookingRequestDto bookingRequestDto);

    String cancelBooking(Long bookingId, String userId);

    BookingResponseDto updateBooking(Long bookingId, BookingRequestDto bookingRequestDto);

    List<BookingResponseDto> listAllBookings();

}
