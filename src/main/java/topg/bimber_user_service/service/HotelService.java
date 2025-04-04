package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.CreateHotelDto;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.HotelResponseDto;

import java.util.List;

public interface HotelService {
    HotelResponseDto createHotel(CreateHotelDto createHotelDto);
    List<HotelDtoFilter> getHotelsByState(String stateName);
    HotelResponseDto editHotelById(Long id, HotelRequestDto hotelRequestDto);
    HotelDtoFilter getHotelById(Long id);
    String deleteHotelById(Long id);
    List<HotelDtoFilter> getTotalHotelsInState(String state);

    List<HotelDtoFilter> getMostBookedHotelsByState(String stateName);
}
