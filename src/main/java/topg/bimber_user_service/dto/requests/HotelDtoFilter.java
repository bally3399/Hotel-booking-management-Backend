package topg.bimber_user_service.dto.requests;

import topg.bimber_user_service.models.Location;

import java.util.List;

public record HotelDtoFilter(
        Long id,
        String name,
        Location location,
        List<String> amenities,
        String description,
        List<String> pictureUrls
)   {


}
