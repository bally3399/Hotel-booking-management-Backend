package topg.bimber_user_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import topg.bimber_user_service.dto.requests.RoomRequest;
import topg.bimber_user_service.dto.responses.RoomResponse;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.RoomType;
import topg.bimber_user_service.models.State;
import topg.bimber_user_service.repository.HotelRepository;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class RoomServiceImplTest {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomServiceImpl roomServiceImpl;

    @Test
    @DisplayName("Room can be created successfully with a picture")
    public void createRoomWithPictureTest() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Ipaja");
        hotel.setState(State.LAGOS);
        hotel.setDescription(" hotel");
        hotel = hotelRepository.save(hotel);

        RoomRequest request = new RoomRequest();
        request.setHotelId(hotel.getId());
        request.setRoomType(RoomType.DELUXE);
        request.setPrice(new BigDecimal("50000.00"));
        request.setAvailable(true);

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "room.jpg",
                "image/jpeg",
                new byte[10]
        );

        RoomResponse response = roomServiceImpl.createRoom(request, Collections.singletonList(mockFile));

        assertNotNull(response);
        assertEquals(RoomType.DELUXE, response.getRoomType());
        assertEquals(new BigDecimal("200.00"), response.getPrice());
        assertTrue(response.isAvailable());
        assertFalse(response.getPictureUrls().isEmpty());
    }

}

