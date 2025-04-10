package topg.bimber_user_service.dto.requests;

import topg.bimber_user_service.models.Location;

import java.io.Serializable;
import java.util.List;

public record HotelDto(
        Long id,
        String name,
        Location location,
        List<String> amenities,
        String description,
        List<String> pictures
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
