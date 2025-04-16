package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.CreateHotelDto;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.HotelResponseDto;
import topg.bimber_user_service.models.Location;

import java.util.List;

public interface HotelService {
    HotelResponseDto createHotel(CreateHotelDto createHotelDto);
    List<HotelDtoFilter> getHotelsByLocation(Location location);
    HotelResponseDto editHotelById(Long id, HotelRequestDto hotelRequestDto);
    HotelDtoFilter getHotelById(Long id);
    HotelDtoFilter findByName(String name);
    List<HotelDtoFilter> getAllHotels();
    String deleteHotelById(Long id);
    List<HotelDtoFilter> getTotalHotelsByLocation(Location location);

    List<HotelDtoFilter> getMostBookedHotelsByLocation(Location location);
}
