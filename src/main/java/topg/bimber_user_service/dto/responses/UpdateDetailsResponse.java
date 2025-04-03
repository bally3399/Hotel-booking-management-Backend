package topg.bimber_user_service.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateDetailsResponse {
    private String message;
    private String firstName;
    private String lastName;
}
