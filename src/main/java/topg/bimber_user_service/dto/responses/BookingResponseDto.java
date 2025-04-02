package topg.bimber_user_service.dto.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponseDto(
        Long bookingId,
        String userId,
        Long roomId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String status,
        boolean isPaid
)  {


}