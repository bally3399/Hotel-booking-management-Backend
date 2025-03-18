package topg.bimber_user_service.dto.requests;

import java.time.LocalDate;

public record BookingRequestDto(
        String userId,
        Long roomId,
        Long hotelId,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isPaid
) {
}
