package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.CreateHotelDto;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.HotelResponseDto;

import java.util.List;

public interface HotelService {
    HotelResponseDto createHotel(CreateHotelDto createHotelDto);
    List<HotelDtoFilter> getHotelsByLocation(String stateName);
    HotelResponseDto editHotelById(Long id, HotelRequestDto hotelRequestDto);
    HotelDtoFilter getHotelById(Long id);
    HotelDtoFilter findByName(String name);
    List<HotelDtoFilter> getAllHotels();
    String deleteHotelById(Long id);
    List<HotelDtoFilter> getTotalHotelsByLocation(String state);

    List<HotelDtoFilter> getMostBookedHotelsByLocation(String stateName);
}
