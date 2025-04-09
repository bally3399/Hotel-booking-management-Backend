
package topg.bimber_user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.responses.CommentResponse;
import topg.bimber_user_service.exceptions.InvalidDetailsException;
import topg.bimber_user_service.exceptions.UnauthorizedException;
import topg.bimber_user_service.service.CommentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {


    private final CommentServiceImpl commentService;


    @PostMapping("/add")
    public ResponseEntity<CommentResponse> addComment(
            @RequestParam String userId,
            @RequestParam Long hotelId,
            @RequestParam String content) {

        CommentResponse commentResponse = commentService.addComment(userId, hotelId, content);

        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }


    @PostMapping("/check-prohibited-words")
    public ResponseEntity<String> checkForProhibitedWords(@RequestParam String content) {
        boolean hasProhibitedWords = commentService.containsProhibitedWords(content);

        if (hasProhibitedWords) {
            return new ResponseEntity<>("Content contains prohibited words", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Content is clean", HttpStatus.OK);
    }


    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByHotel(@PathVariable Long hotelId) {
        List<CommentResponse> comments = commentService.getCommentsByHotel(hotelId);
        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUser(@PathVariable String userId) {
        List<CommentResponse> comments = commentService.getCommentsByUser(userId);
        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{hotelId}/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long hotelId,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "anonymous") String userId) {

        try {
            String responseMessage = commentService.deleteComment(hotelId, commentId, userId);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (InvalidDetailsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


}

