package topg.bimber_user_service.dto.requests;

import lombok.Getter;
import lombok.Setter;
import topg.bimber_user_service.models.RoomType;

import java.math.BigDecimal;
@Getter
@Setter
public class RoomRequest{
      private RoomType roomType;
      private BigDecimal price;
      private Boolean available;
      private Long hotelId;

}
