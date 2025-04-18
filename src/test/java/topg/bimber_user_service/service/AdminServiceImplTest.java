package topg.bimber_user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort; // Add this import
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.*;
import topg.bimber_user_service.repository.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static topg.bimber_user_service.models.Location.*;
import static topg.bimber_user_service.models.Role.ADMIN;

@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @LocalServerPort
    private int port;

    private String jwtToken;

    public void setUp() {
        String baseUrl = "http://localhost:" + port + "/api/v1/auth";
        System.out.println("Attempting login at: " + baseUrl);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("Password@123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<BaseResponse<LoginResponse>> response = testRestTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<BaseResponse<LoginResponse>>() {}
        );

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Login request failed");
        BaseResponse<LoginResponse> baseResponse = response.getBody();
        assertNotNull(baseResponse, "Base response is null");
        assertTrue(baseResponse.isSuccess(), "Authentication was not successful");
        LoginResponse loginResponse = baseResponse.getData();
        assertNotNull(loginResponse, "Login response is null");
        jwtToken = loginResponse.getJwtToken();
        assertNotNull(jwtToken, "JWT token is null");
    }

    @Test
    public void createAdminTest() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("newadmin@example.com");
        userRequestDto.setPassword("Password@123");
        userRequestDto.setUsername("newadmin");

        UserCreatedDto userCreatedDto = adminService.createAdmin(userRequestDto);
        assertThat(userCreatedDto.getMessage()).isEqualTo("Admin registered successfully");
    }


//    @Test
//    @Transactional
//    public void testThatAdminCanAddRoom() {
//        RoomRequest roomRequest = new RoomRequest();
//        roomRequest.setRoomType(RoomType.DELUXE);
//        roomRequest.setPrice(new BigDecimal("50000"));
//        roomRequest.setIsAvailable(true);
//        roomRequest.setHotelId(1L);
//
//        MockMultipartFile mockImage = createMockImage();
//        List<String> mockFiles = Collections.singletonList(mockImage.getOriginalFilename());
//
//        RoomResponse response = adminService.addRoom(roomRequest, mockFiles);
//        assertResponse(response);
//    }
//
//    @Test
//    @Transactional
//    public void testThatAdminCanAddHotel() {
//        CreateHotelDto createHotelDto = CreateHotelDto.builder()
//                .name("New Hotel")
//                .amenities(List.of("Gym", "Pool"))
//                .description("A new hotel for testing.")
//                .pictures(List.of("pic1.jpg", "pic2.jpg"))
//                .location(BELFAST)
//                .build();
//
//        HotelResponseDto response = adminService.createHotel(createHotelDto);
//        assertNotNull(response);
//    }

    @Test
    public void testThatAdminCanGetHotelByLocation() {
        List<HotelDtoFilter> hotels = adminService.getHotelsByLocation(ABERDEEN);
        assertNotNull(hotels);
        assertFalse(hotels.isEmpty());
        assertEquals(1, hotels.size());
        assertEquals("Grand Royale", hotels.get(0).name());
    }

    @Test
    @Transactional
    public void testThatAdminCanEditHotelById() {
        HotelRequestDto updatedHotelDto = HotelRequestDto.builder()
                .name("Grand Royale Updated")
                .amenities(List.of("Spa", "Bar"))
                .description("Updated description")
                .pictures(List.of("newpic.jpg"))
                .location(ABERDEEN)
                .build();

        HotelResponseDto response = adminService.editHotelById(1L, updatedHotelDto);
        assertNotNull(response);
    }

    @Test
    public void testThatAdminCanGetHotelById() {
        HotelDtoFilter response = adminService.getHotelById(1L);
        assertNotNull(response);
        assertEquals("Grand Royale", response.name());
    }

    @Test
    @Transactional
    public void testThatAdminCanDeleteHotelById() {
        adminService.deleteHotelByName("Grand Royale");
        Hotel deletedHotel = hotelRepository.findByName("Grand Royale").orElse(null);
        assertNull(deletedHotel);
    }

    @Test
    public void testThatAdminCanGetMostBookedHotelByLocation() {
        List<HotelDtoFilter> response = adminService.getMostBookedHotelByLocation(BELFAST);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals("Sunset Sands", response.get(0).name());
    }

    @Test
    @Transactional
    public void testThatAdminCanEditRoomById() {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setRoomType(RoomType.SUITE);
        roomRequest.setPrice(new BigDecimal("75000"));
        roomRequest.setIsAvailable(false);
        roomRequest.setHotelName("Grand Royale");

        EditRoomResponse response = adminService.editRoomById(1L, roomRequest);
        assertNotNull(response);

        Room updatedRoom = roomRepository.findById(1L).orElseThrow();
        assertEquals(RoomType.SUITE, updatedRoom.getRoomType());
        assertEquals(new BigDecimal("75000"), updatedRoom.getPrice());
        assertFalse(updatedRoom.isAvailable());
    }

    @Test
    @Transactional
    public void testThatAdminCanDeleteRoomById() {
        adminService.deleteRoomById(1L);
        Room deletedRoom = roomRepository.findById(1L).orElse(null);
        assertNull(deletedRoom);
    }

    @Test
    public void testThatAdminCanFindAllRoomsByHotelId() {
        List<RoomResponse> rooms = adminService.findAllRoomsByHotelId(1L);
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomType() == RoomType.DELUXE));
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomType() == RoomType.SINGLE));
    }

    @Test
    public void testThatAdminCanCheckIfRoomIsAvailable() {
        boolean isAvailable = adminService.isRoomAvailable(1L);
        assertTrue(isAvailable);
    }

    @Test
    public void testThatAdminCanFindAllAvailableHotelRooms() {
        List<RoomResponse> availableRooms = adminService.findAllAvailableHotelRooms(1L);
        assertNotNull(availableRooms);
        assertEquals(2, availableRooms.size());
        assertTrue(availableRooms.stream().allMatch(RoomResponse::isAvailable));
    }

    @Test
    @Transactional
    public void testThatAdminCanDeactivateRoomByHotelId() {
        RoomResponse response = adminService.deactivateRoomByHotelId(1L, 1L);
        assertNotNull(response);
        assertFalse(response.isAvailable());

        Room room = roomRepository.findById(1L).orElseThrow();
        assertFalse(room.isAvailable());
    }

    @Test
    @Transactional
    public void testThatAdminCanActivateRoomByHotelId() {
        adminService.deactivateRoomByHotelId(1L, 1L);
        RoomResponse response = adminService.activateRoomByHotelId(1L, 1L);
        assertNotNull(response);
        assertTrue(response.isAvailable());

        Room room = roomRepository.findById(1L).orElseThrow();
        assertTrue(room.isAvailable());
    }

    @Test
    public void testThatAdminCanFilterHotelRoomByType() {
        List<RoomResponse> deluxeRooms = adminService.filterHotelRoomByType(1L, "DELUXE");
        assertNotNull(deluxeRooms);
        assertEquals(1, deluxeRooms.size());
        assertEquals(RoomType.DELUXE, deluxeRooms.get(0).getRoomType());
    }

//    @Test
//    public void testThatAdminCanFilterByPriceAndLocation() {
//        List<RoomResponse> rooms = adminService.filterByPriceAndLocation(
//                new BigDecimal("30000"), new BigDecimal("60000"), ABERDEEN);
//        assertNotNull(rooms);
//        assertTrue(rooms.stream().anyMatch(r -> r.getPrice().equals(new BigDecimal("50000"))));
//        assertTrue(rooms.stream().anyMatch(r -> r.getPrice().equals(new BigDecimal("35000"))));
//    }

    private MockMultipartFile createMockImage() {
        return new MockMultipartFile("file", "room.jpg", "image/jpeg", new byte[10]);
    }

    private void assertResponse(RoomResponse response) {
        assertNotNull(response);
        assertEquals(RoomType.DELUXE, response.getRoomType());
        assertEquals(new BigDecimal("50000"), response.getPrice());
        assertTrue(response.isAvailable());
    }
}