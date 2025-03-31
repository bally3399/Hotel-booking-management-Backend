package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.responses.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(String userId, Long hotelId, String content);

    boolean containsProhibitedWords(String content);

    List<CommentResponse> getCommentsByHotel(Long hotelId);

    List<CommentResponse>getCommentsByUser(String userId);

    String deleteComment(Long hotelId, Long commentId, String userId);
}
