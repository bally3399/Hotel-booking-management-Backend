package topg.bimber_user_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import topg.bimber_user_service.dto.responses.CommentResponse;
import topg.bimber_user_service.exceptions.InvalidDetailsException;
import topg.bimber_user_service.exceptions.UnauthorizedException;
import topg.bimber_user_service.models.Comment;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.State;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.repository.CommentRepository;
import topg.bimber_user_service.repository.HotelRepository;
import topg.bimber_user_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CommentServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private CommentRepository commentRepository;


    @Test
    @DisplayName("Should add a comment successfully")
    public void shouldAddCommentSuccessfully() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        user = userRepository.save(user);
        assertNotNull(user.getId());


        UUID userId = UUID.fromString(user.getId());


        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test City");
        hotel.setDescription("A great place to stay");
        hotel = hotelRepository.save(hotel);
        assertNotNull(hotel.getId());

        Long hotelId = hotel.getId();


        String content = "Great hotel!";
        CommentResponse response = commentService.addComment(userId.toString(), hotelId, content);


        assertNotNull(response);
        assertEquals(userId.toString(), response.getUserId()); // Ensure correct type comparison
        assertEquals(hotelId, response.getHotelId());
        assertEquals(content, response.getContent());
        assertNotNull(response.getCreatedAt());
    }


    @Test
    void shouldDetectProhibitedWords() {
        assertTrue(commentService.containsProhibitedWords("This contains badword1"));
        assertTrue(commentService.containsProhibitedWords("offensive content here"));
    }

    @Test
    void shouldNotDetectProhibitedWords() {
        assertFalse(commentService.containsProhibitedWords("This is a clean message"));
        assertFalse(commentService.containsProhibitedWords(""));
        assertFalse(commentService.containsProhibitedWords(null));
    }

    @Test
    void shouldBeCaseInsensitive() {
        assertTrue(commentService.containsProhibitedWords("This contains BADWORD1"));
        assertTrue(commentService.containsProhibitedWords("OfFeNsIvE words here"));
    }

    @Test
    void shouldReturnCommentsForHotel() {

        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(State.LAGOS);
        hotel.setDescription("A nice hotel");
        hotelRepository.saveAndFlush(hotel);

        assertTrue(hotelRepository.existsById(hotel.getId()), "Hotel not found!");

        User user = new User();
        user.setId(UUID.randomUUID().toString());

        Comment comment1 = Comment.builder()
                .hotel(hotel)
                .userId(user.getId())
                .userName("Jhay")
                .content("Great stay!")
                .build();

        Comment comment2 = Comment.builder()
                .hotel(hotel)
                .userId(user.getId())
                .userName("Jumoke")
                .content("Nice service!")
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);


        List<CommentResponse> responses = commentService.getCommentsByHotel(hotel.getId());

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Great stay!", responses.get(0).getContent());
        assertEquals("Nice service!", responses.get(1).getContent());
    }


    @Test
    void shouldReturnEmptyListWhenNoCommentsExist() {
        Long hotelId = 2L;

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setName("Empty Hotel");
        hotel.setLocation("No Comments Location");
        hotel.setState(State.LAGOS);
        hotel.setDescription("No comments yet");

        hotelRepository.save(hotel);

        List<CommentResponse> responses = commentService.getCommentsByHotel(hotelId);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void shouldReturnCommentsByUser() {
        String userId = UUID.randomUUID().toString();

        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(State.LAGOS);
        hotel.setDescription("A nice place");
        hotelRepository.save(hotel);

        Comment comment1 = Comment.builder()
                .hotel(hotel)
                .userId(userId)
                .userName("User1")
                .content("Amazing experience!")
                .build();

        Comment comment2 = Comment.builder()
                .hotel(hotel)
                .userId(userId)
                .userName("User1")
                .content("Would visit again!")
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<CommentResponse> responses = commentService.getCommentsByUser(userId);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Amazing experience!", responses.get(0).getContent());
        assertEquals("Would visit again!", responses.get(1).getContent());
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoComments() {
        String userId = UUID.randomUUID().toString();

        List<CommentResponse> responses = commentService.getCommentsByUser(userId);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void shouldDeleteCommentSuccessfully() {
        String userId = "user123";


        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(State.LAGOS);
        hotel.setDescription("A nice hotel");
        hotelRepository.saveAndFlush(hotel); // Ensures persistence


        assertTrue(hotelRepository.existsById(hotel.getId()), "Hotel was not saved!");


        Comment comment = new Comment();
        comment.setHotel(hotel);
        comment.setUserId(userId);
        comment.setUserName("Test User");
        comment.setContent("Test comment");
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.saveAndFlush(comment); // Ensure it's in DB


        Long commentId = comment.getId();
        assertNotNull(commentId, "Comment ID is null!");


        String result = commentService.deleteComment(hotel.getId(), commentId, userId);


        assertEquals("Comment deleted successfully", result);
        assertFalse(commentRepository.findById(commentId).isPresent(), "Comment was not deleted!");
    }


    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        Long hotelId = 1L;
        Long nonExistentCommentId = 99L;
        String userId = "user123";

        assertThrows(InvalidDetailsException.class, () ->
                commentService.deleteComment(hotelId, nonExistentCommentId, userId)
        );
    }

    @Test
    void shouldThrowExceptionWhenCommentDoesNotBelongToHotel() {
        Long hotelId = 1L;
        Long anotherHotelId = 2L;
        String userId = "user123";

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setName("Test Hotel");
        hotel.setLocation("Location");
        hotel.setState(State.LAGOS);
        hotel.setDescription("A nice hotel");
        hotelRepository.save(hotel);

        Hotel anotherHotel = new Hotel();
        anotherHotel.setId(anotherHotelId);
        anotherHotel.setName("Another Hotel");
        anotherHotel.setLocation("Location");
        anotherHotel.setState(State.LAGOS);
        anotherHotel.setDescription("A nice hotel");
        hotelRepository.save(anotherHotel);

        Comment comment = new Comment();
        comment.setHotel(anotherHotel);
        comment.setUserId(userId);
        comment.setUserName("Test User");
        comment.setContent("Wrong hotel comment");
        commentRepository.save(comment);

        assertThrows(IllegalArgumentException.class, () ->
                commentService.deleteComment(hotelId, comment.getId(), userId)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAuthorizedToDelete() {
        String userId = "user123";
        String anotherUserId = "otherUser";

        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(State.LAGOS);
        hotel.setDescription("A nice place to stay");
        hotel = hotelRepository.save(hotel);


        Comment comment = new Comment();
        comment.setHotel(hotel);
        comment.setUserId(anotherUserId);
        comment.setUserName("Another User");
        comment.setContent("Unauthorized comment");
        comment = commentRepository.save(comment);


        final Long commentId = comment.getId();

        Hotel finalHotel = hotel;
        assertThrows(UnauthorizedException.class, () ->
                commentService.deleteComment(finalHotel.getId(), commentId, userId) // Use final variable
        );
    }







}