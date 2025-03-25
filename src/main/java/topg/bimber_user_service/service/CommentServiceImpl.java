package topg.bimber_user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import topg.bimber_user_service.dto.responses.CommentResponse;
import topg.bimber_user_service.exceptions.InvalidDetailsException;
import topg.bimber_user_service.exceptions.UnauthorizedException;
import topg.bimber_user_service.models.Booking;
import topg.bimber_user_service.models.Comment;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.repository.BookingRepository;
import topg.bimber_user_service.repository.CommentRepository;
import topg.bimber_user_service.repository.HotelRepository;
import topg.bimber_user_service.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    private final List<String> prohibitedWords = List.of("badword1", "badword2", "offensive");



    @Override
    public CommentResponse addComment(String userId, Long hotelId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setUserName(user.getUsername());
        comment.setHotel(hotel);
        comment.setContent(content);

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getUserName(),
                savedComment.getUserId(),
                savedComment.getHotel().getId()
        );
    }




    @Override
    public boolean containsProhibitedWords(String content) {

        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        String lowerCaseContent = content.toLowerCase();
        return prohibitedWords.stream().anyMatch(lowerCaseContent::contains);
    }

    @Override
    public List<CommentResponse> getCommentsByHotel(Long hotelId) {
        List<Comment> comments = commentRepository.findByHotelId(hotelId);
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUserName(),
                        comment.getUserId(),
                        comment.getHotel().getId()
                ))
                .collect(Collectors.toList());
    }




    @Override
    public List<CommentResponse> getCommentsByUser(String userId) {
        List<Comment> comments = commentRepository.findByUserId(userId); // No need for casting
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUserName(),
                        comment.getUserId(),
                        comment.getHotel().getId()
                ))

                .collect(Collectors.toList());
    }


    @Override
    public String deleteComment(Long hotelId, Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new InvalidDetailsException("Comment not found"));

        if (!comment.getHotel().getId().equals(hotelId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified hotel");
        }

        if (!comment.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }




}
