package topg.bimber_user_service.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import topg.bimber_user_service.models.RoomType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class BookingRequestDto {
    private RoomType roomType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String hotelName;

}

