package topg.bimber_user_service.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import topg.bimber_user_service.models.Room;
import topg.bimber_user_service.models.Comment;
import topg.bimber_user_service.models.State;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
@Builder
public class HotelRequestDto{
        private String name;
        private State state;
        private String location;
        private String description;
        private List<String> amenities;
        private List<Room> rooms;
        private List<Comment> comments;
        private List<String> pictures;
}
