package topg.bimber_user_service.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import topg.bimber_user_service.models.State;

import java.util.List;
@Getter
@Setter
@Builder
public class CreateHotelDto {
    private String name;
    private State state;
    private String description;
    private String location;
    private List<String> amenities;
    private List<String> pictures;
}
