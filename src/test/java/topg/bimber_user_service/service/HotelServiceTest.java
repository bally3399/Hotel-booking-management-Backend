package topg.bimber_user_service.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import topg.bimber_user_service.dto.requests.CreateHotelDto;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.HotelResponseDto;
import topg.bimber_user_service.exceptions.InvalidUserInputException;
import topg.bimber_user_service.models.Location;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static topg.bimber_user_service.models.Location.BELFAST;

@SpringBootTest
@Sql(scripts = {"/db/data.sql"})
@Slf4j
class HotelServiceTest {
    @Autowired
    private HotelService hotelService;
    private CreateHotelDto createHotelDto;

    @BeforeEach
    public void setUp() {
        createHotelDto = CreateHotelDto.builder()
                .name("Mike Hotel")
                .amenities(List.of("Gym", "Club House", "Pool"))
                .description("A luxurious hotel in Belfast")
                .pictures(List.of("picture1.jpg", "picture2.jpg", "picture3.jpg"))
                .location(BELFAST)
                .build();
    }

    @Test
    void createHotel() {
        HotelResponseDto response = hotelService.createHotel(createHotelDto);
        assertNotNull(response);
        assertNotNull(response.hotel().id());
        assertEquals("Mike Hotel", response.hotel().name());
        assertEquals(BELFAST, response.hotel().location());
        assertEquals("A luxurious hotel in Belfast", response.hotel().description());
        assertThat(response.hotel().amenities()).containsExactly("Gym", "Club House", "Pool");
        assertThat(response.hotel().pictures()).containsExactly("picture1.jpg", "picture2.jpg", "picture3.jpg");
        log.info("Created hotel: {}", response);
    }

    @Test
    void getHotelsByLocation() {
        List<HotelDtoFilter> response = hotelService.getHotelsByLocation(BELFAST);
        assertNotNull(response);
        assertThat(response.size()).isEqualTo(1);
        assertEquals("Sunset Sands", response.get(0).name());
        assertEquals(BELFAST, response.get(0).location());
        log.info("Hotels in Belfast: {}", response);
    }

    @Test
    void editHotelById() {
        HotelRequestDto dto = HotelRequestDto.builder()
                .name("New Sunset Sands")
                .description("Updated description")
                .build();
        HotelResponseDto response = hotelService.editHotelById(2L, dto);
        assertNotNull(response);
        assertEquals("New Sunset Sands", response.hotel().name());
        assertEquals("Updated description", response.hotel().description());
        log.info("Updated hotel: {}", response);
    }

    @Test
    void getHotelById() {
        HotelDtoFilter response = hotelService.getHotelById(1L);
        assertNotNull(response);
        assertEquals("Grand Royale", response.name());
        assertEquals("Granite City in the northeast, oil industry hub.", response.description());
        assertEquals(Location.ABERDEEN, response.location());
        log.info("Hotel by ID: {}", response);
    }

    @Test
    void deleteHotelByName() {
        // Create a hotel to delete
        HotelResponseDto created = hotelService.createHotel(createHotelDto);
        String hotelName = created.hotel().name();

        hotelService.deleteHotelByName(hotelName);

        assertThrows(InvalidUserInputException.class, () -> hotelService.getHotelByName(hotelName));
        log.info("Deleted hotel: {}", hotelName);
    }

    @Test
    void getTotalHotelsByLocation() {
        List<HotelDtoFilter> response = hotelService.getTotalHotelsByLocation(BELFAST);
        assertNotNull(response);
        assertThat(response.size()).isEqualTo(1);
        assertEquals("Sunset Sands", response.get(0).name());
        log.info("Total hotels in Belfast: {}", response);
    }

    @Test
    void getMostBookedHotelsByLocation() {
        List<HotelDtoFilter> response = hotelService.getMostBookedHotelsByLocation(BELFAST);
        assertNotNull(response);
        assertThat(response.size()).isEqualTo(1);
        assertEquals("Sunset Sands", response.get(0).name());
        log.info("Most booked hotels in Belfast: {}", response);
    }
}