//package topg.bimber_user_service.dto.requests;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import topg.bimber_user_service.models.RoomType;
//
//import java.math.BigDecimal;
//@Getter
//@Setter
////@AllArgsConstructor
//public class RoomRequest{
//      private RoomType roomType;
//      private BigDecimal price;
//      private Boolean isAvailable;
//      private Long hotelId;
//
//
//}

package topg.bimber_user_service.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import topg.bimber_user_service.models.RoomType;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomRequest {
      private RoomType roomType;
      private BigDecimal price;
      private Boolean isAvailable;
      private String hotelName;
      private List<String> pictures;
}
