package topg.bimber_user_service.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import topg.bimber_user_service.models.RoomType;

import java.math.BigDecimal;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditRoomResponse {

    private boolean success;
    private String message;
    private Long roomId;
    private RoomType roomType;
    private BigDecimal price;
    private Boolean isAvailable;
//    private List<String> pictures;
}
