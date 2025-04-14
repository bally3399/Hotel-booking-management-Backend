package topg.bimber_user_service.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import topg.bimber_user_service.models.Location;

import java.util.List;
@Getter
@Setter
@Builder
public class CreateHotelDto {
    private String name;
    private Location location;
    private String description;
    private List<String> amenities;
    private List<String> pictures;
}
