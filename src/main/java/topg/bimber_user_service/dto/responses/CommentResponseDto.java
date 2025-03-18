package topg.bimber_user_service.dto.responses;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        String username
) {
}
