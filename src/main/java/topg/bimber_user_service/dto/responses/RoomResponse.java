package topg.bimber_user_service.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.Room;
import topg.bimber_user_service.models.RoomPicture;
import topg.bimber_user_service.models.RoomType;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse{
       private Long id;
       private Long hotelId;
       private RoomType roomType;
       private BigDecimal price;
       private boolean isAvailable;
       private List<String> pictureUrls;
}

