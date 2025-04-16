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

@SpringBootTest
@Sql(scripts ={"/db/data.sql"} )
@Slf4j
class HotelServiceTest {
    @Autowired
    private HotelService hotelService;
    private CreateHotelDto createHotelDto;

    @BeforeEach
    public void setUp(){
        createHotelDto = CreateHotelDto.builder()
                .name("mike")
                .amenities(List.of("Gym", "club house", "pool"))
                .description("description")
                .pictures(List.of("picture1", "picture2","picture3"))
               .location(Location.BELFAST)
                .build();

    }

    @Test
    void createHotel() {
        var response = hotelService.createHotel(createHotelDto);
        assertNotNull(response);
    }

    @Test
    void getHotelsByLocation() {
        List<HotelDtoFilter> response = hotelService.getHotelsByLocation(Location.BELFAST);
        assertNotNull(response);
        assertThat(response.size()).isEqualTo(1);
    }

    @Test
    void editHotelById() {
        HotelRequestDto dto =HotelRequestDto.builder()
                .name("newName")
                .description("description")
                .build();
        HotelResponseDto response = hotelService.editHotelById(1L,dto);
        assertNotNull(response);
        assertThat(response.hotel().description()).isEqualTo("description");
    }

    @Test
    void getHotelById() {
        HotelDtoFilter response = hotelService.getHotelById(1L);
        assertNotNull(response);
        assertThat(response.description()).isEqualTo("Granite City in the northeast, oil industry hub.");

    }

    @Test
    void deleteHotelById() {
        var response = hotelService.deleteHotelById(1L);
        assertThrows(InvalidUserInputException.class,()-> hotelService.deleteHotelById(1L));
    }

    @Test
    void getTotalHotelsByLocation() {
        List<HotelDtoFilter> response  = hotelService.getTotalHotelsByLocation(Location.BELFAST);
        assertThat(response.size()).isEqualTo(1);

    }

    @Test
    void getMostBookedHotelsByLocation() {
        var response = hotelService.getMostBookedHotelsByLocation(Location.BELFAST);
        assertThat(response.size()).isEqualTo(1);
    }
}