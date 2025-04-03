package topg.bimber_user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.Admin;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.RoomType;
import topg.bimber_user_service.models.State;
import topg.bimber_user_service.repository.AdminRepository;
import topg.bimber_user_service.repository.HotelRepository;
import topg.bimber_user_service.repository.TokenRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static topg.bimber_user_service.models.Role.ADMIN;
import static topg.bimber_user_service.models.State.OSUN;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@SpringBootTest
public class AdminServiceImplTest {
    @Autowired
    private AdminService adminService;
    @Autowired
    private TokenRepository tokenRepository;
    private UserCreatedDto userCreatedDto;
    private LoginResponse loginResponse;
    private CreateHotelDto createHotelDto;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private HotelRepository hotelRepository;

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
        loginResponse = adminService.login(loginRequest);


    }

    @Test
    public void createAdminTest(){
        assertThat(userCreatedDto.getMessage()).isEqualTo("Admin registered successfully");
    }

    @Test
    public void testThatAdminCanLogin(){
        assertEquals("Login Successful", loginResponse.getMessage());
    }

    @Test
    public void testThatAdminCanAddRoom() {
        Admin admin = createTestAdmin();
        adminRepository.save(admin);

        Hotel hotel = createTestHotel();
        hotelRepository.save(hotel);

        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setRoomType(RoomType.DELUXE);
        roomRequest.setPrice(new BigDecimal("50000"));
        roomRequest.setIsAvailable(true);
        roomRequest.setHotelId(hotel.getId());

        MockMultipartFile mockImage = createMockImage();
        List<String> mockFiles = Collections.singletonList(mockImage.getOriginalFilename());

        RoomResponse response = adminService.addRoom(roomRequest, mockFiles);

        assertResponse(response);
    }

    private Admin createTestAdmin() {
        Admin admin = new Admin();
        admin.setId(String.valueOf(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
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
        hotel.setId(1L);
        hotel.setName("Grand Royale");
        hotel.setState(OSUN);
        hotel.setLocation("Osogbo");
        hotel.setDescription("A luxurious hotel located in the heart of Osogbo.");
        hotel.setComments(Collections.emptyList());
        hotel.setRooms(Collections.emptyList()); // Initialize rooms collection
        return hotel;
    }

    private MockMultipartFile createMockImage() {
        return new MockMultipartFile(
                "file",
                "room.jpg",
                "image/jpeg",
                new byte[10]
        );
    }

    private void assertResponse(RoomResponse response) {
        assertNotNull(response);
        assertEquals(RoomType.DELUXE, response.getRoomType());
        assertEquals(new BigDecimal("50000"), response.getPrice());
        assertTrue(response.isAvailable());
    }

    @Test
    public void testThatAdminCanAddHotel() {
        Admin admin = new Admin();
        admin.setId(String.valueOf(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        admin.setUsername("admin_user");
        admin.setEmail("admin@example.com");
        admin.setPassword("hashed_password_here");
        admin.setRole(ADMIN);
        admin.setEnabled(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminRepository.save(admin);

        createHotelDto = CreateHotelDto.builder()
                .name("mike")
                .amenities(List.of("Gym", "club house", "pool"))
                .description("description")
                .pictures(List.of("picture1", "picture2", "picture3"))
                .location("Lagos Island")
                .state(State.LAGOS)
                .build();

        HotelResponseDto response = adminService.createHotel(createHotelDto);

        assertNotNull(response);
    }
    @Test
    public void testThatAdminCanGetHotelByState(){

    }

    @Test
    public void testThatAdminCanBeUpdated() {
        Admin existingAdmin = new Admin();
        existingAdmin.setEmail("johnny@doe.com");
        existingAdmin.setPassword("Password@123");
        existingAdmin.setUsername("johnny");
        adminRepository.save(existingAdmin);

        UpdateDetailsRequest updateUserRequest = new UpdateDetailsRequest();
        updateUserRequest.setEmail("john@doe.com");
        updateUserRequest.setPassword("@Password12");

        UpdateDetailsResponse response = adminService.updateAdmin(updateUserRequest);

        assertEquals("Updated successfully", response.getMessage());

        Admin updatedAdmin = adminService.findByEmail("john@doe.com");
        assertThat(updatedAdmin).isNotNull();
        assertEquals("@Password12", updatedAdmin.getPassword());
    }

}



