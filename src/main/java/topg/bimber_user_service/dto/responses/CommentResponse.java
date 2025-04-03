package topg.bimber_user_service.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import topg.bimber_user_service.models.User;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse{
       private Long id;
       private String content;
       private LocalDateTime createdAt;
       private User user;
       private Long hotelId;

}
