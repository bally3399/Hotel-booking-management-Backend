package topg.bimber_user_service.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.processing.SQL;
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
import topg.bimber_user_service.models.State;

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
                .location("Lagos Island")
                .state(State.LAGOS)
                .build();

    }

    @Test
    void createHotel() {
        var response = hotelService.createHotel(createHotelDto);
        assertNotNull(response);
    }

    @Test
    void getHotelsByState() {
        List<HotelDtoFilter> response = hotelService.getHotelsByState("LAGOS");
        assertNotNull(response);
        assertThat(response.size()).isEqualTo(2);
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
        assertThat(response.description()).isEqualTo("A luxurious hotel located in the heart of Osogbo.");

    }

    @Test
    void deleteHotelById() {
        var response = hotelService.deleteHotelById(1L);
        assertThrows(InvalidUserInputException.class,()-> hotelService.deleteHotelById(1L));
    }

    @Test
    void getTotalHotelsInState() {
        List<HotelDtoFilter> response  = hotelService.getTotalHotelsInState("LAGOS");
        assertThat(response.size()).isEqualTo(2);

    }

    @Test
    void getMostBookedHotelsByState() {
        var response = hotelService.getMostBookedHotelsByState("LAGOS");
        assertThat(response.size()).isEqualTo(0);
    }
}