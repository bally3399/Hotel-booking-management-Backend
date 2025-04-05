package topg.bimber_user_service.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class BookingRequestDto {
    private String userId;
    private Long roomId;
    private Long hotelId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isPaid;


}

