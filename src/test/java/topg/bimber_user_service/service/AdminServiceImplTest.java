package topg.bimber_user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.*;
import topg.bimber_user_service.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static topg.bimber_user_service.models.Role.ADMIN;
import static topg.bimber_user_service.models.State.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = "/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
public class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private UserCreatedDto userCreatedDto;
    private LoginResponse loginResponse;

    @BeforeEach
    public void setUp() {
        tokenRepository.deleteAll();
        adminService.deleteAll();
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("john@doe.com");
        userRequestDto.setPassword("Password@123");
        userRequestDto.setUsername("johny");
        userCreatedDto = adminService.createAdmin(userRequestDto);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@doe.com");
        loginRequest.setPassword("Password@123");
    }

    // Existing Tests (unchanged for brevity, assuming they work as intended)
    @Test
    public void createAdminTest() {
        assertThat(userCreatedDto.getMessage()).isEqualTo("Admin registered successfully");
    }

    @Test
    public void testThatAdminCanLogin() {
        assertEquals("Login Successful", loginResponse.getMessage());
    }

    @Test
    @Transactional
    public void testThatAdminCanAddRoom() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setState(OSUN);
        hotel.setLocation("Test Location");
        hotel.setDescription("A test hotel.");
        hotel.setComments(Collections.emptyList());
        hotel.setRooms(Collections.emptyList());
        hotel.setAmenities(List.of("Test Amenity"));
        Hotel savedHotel = hotelRepository.save(hotel);

        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setRoomType(RoomType.DELUXE);
        roomRequest.setPrice(new BigDecimal("50000"));
        roomRequest.setIsAvailable(true);
        roomRequest.setHotelId(savedHotel.getId());

        MockMultipartFile mockImage = createMockImage();
        List<String> mockFiles = Collections.singletonList(mockImage.getOriginalFilename());

        RoomResponse response = adminService.addRoom(roomRequest, mockFiles);
        assertResponse(response);
    }

    @Test
    @Transactional
    public void testThatAdminCanAddHotel() {
        CreateHotelDto createHotelDto = CreateHotelDto.builder()
                .name("Mike Hotel")
                .amenities(List.of("Gym", "Club House", "Pool"))
                .description("A new hotel for testing.")
                .pictures(List.of("picture1", "picture2", "picture3"))
                .location("Lagos Island")
                .state(LAGOS)
                .build();

        HotelResponseDto response = adminService.createHotel(createHotelDto);
        assertNotNull(response);
//        assertEquals("Mike Hotel", response.getName()); // Strengthen assertion
    }

    @Test
    public void testThatAdminCanGetHotelByState() {
        List<HotelDtoFilter> hotels = adminService.getHotelsByState(OSUN);
        assertNotNull(hotels);
        assertFalse(hotels.isEmpty());
        assertEquals(1, hotels.size());
        assertEquals("Grand Royale", hotels.get(0).name());
    }

    @Test
    @Transactional
    public void testThatAdminCanEditHotelById() {
        Hotel hotel = hotelRepository.findById(1L).orElseThrow();
        HotelRequestDto updatedHotelDto = HotelRequestDto.builder()
                .name("Grand Royale Deluxe")
                .amenities(List.of("Spa", "Bar"))
                .description("Updated description")
                .pictures(List.of("newpic.jpg"))
                .location("Osogbo Downtown")
                .state(OSUN)
                .build();

        HotelResponseDto response = adminService.editHotelById(hotel.getId(), updatedHotelDto);
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
        adminService.deleteHotelById(1L);
        Hotel deletedHotel = hotelRepository.findById(1L).orElse(null);
        assertNull(deletedHotel);
    }

    @Test
    @Transactional
    public void testThatAdminCanGetMostBookedHotelInState() {
        List<HotelDtoFilter> osunResponse = adminService.getMostBookedHotelInState(OSUN);
        assertNotNull(osunResponse);
        assertFalse(osunResponse.isEmpty());
        assertEquals("Grand Royale", osunResponse.get(0).name());

        List<HotelDtoFilter> ogunResponse = adminService.getMostBookedHotelInState(OGUN);
        assertNotNull(ogunResponse);
        assertFalse(ogunResponse.isEmpty());
        assertEquals("Sunset Sands", ogunResponse.get(0).name());
    }

    @Test
    public void testThatAdminCanGetHotelsInState() {
        List<HotelDtoFilter> hotels = adminService.getHotelsInState(OSUN);
        assertNotNull(hotels);
        assertEquals(1, hotels.size());
        assertEquals("Grand Royale", hotels.get(0).name());
    }

    @Test
    @Transactional
    public void testThatAdminCanEditRoomById() {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setRoomType(RoomType.SUITE);
        roomRequest.setPrice(new BigDecimal("75000"));
        roomRequest.setIsAvailable(false);
        roomRequest.setHotelId(1L);

        String response = adminService.editRoomById(1L, roomRequest);
        assertNotNull(response);
        assertEquals("Room updated successfully", response);

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

    @Test
    public void testThatAdminCanFilterByPriceAndState() {
        List<RoomResponse> rooms = adminService.filterByPriceAndState(
                new BigDecimal("30000"), new BigDecimal("60000"), OSUN);
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
//        assertTrue(rooms.stream().anyMatch(r -> r.getPrice().equals(new BigDecimal("50000"))));
//        assertTrue(rooms.stream().anyMatch(r -> r.getPrice().equals(new BigDecimal("35000"))));
    }

    private Admin createTestAdmin() {
        Admin admin = new Admin();
        admin.setId("123e4567-e89b-12d3-a456-426614174000");
        admin.setUsername("admin_user");
        admin.setEmail("admin@example.com");
        admin.setPassword("hashed_password_here");
        admin.setRole(ADMIN);
        admin.setEnabled(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        return admin;
    }

    private Hotel createTestHotel() {
        Hotel hotel = new Hotel();
        hotel.setId(5L); // Use a new ID to avoid conflicts with data.sql
        hotel.setName("Test Hotel");
        hotel.setState(OSUN);
        hotel.setLocation("Test Location");
        hotel.setDescription("A test hotel.");
        hotel.setComments(Collections.emptyList());
        hotel.setRooms(Collections.emptyList());
        hotel.setAmenities(List.of("Test Amenity"));
        return hotel;
    }

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