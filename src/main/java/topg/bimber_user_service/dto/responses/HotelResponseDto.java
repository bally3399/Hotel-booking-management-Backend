package topg.bimber_user_service.dto.responses;

import topg.bimber_user_service.dto.requests.HotelDto;

import java.io.Serializable;
import java.util.List;

public record HotelResponseDto(
        boolean success,
        HotelDto hotel
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
