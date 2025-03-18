package topg.bimber_user_service.service;

import org.springframework.web.multipart.MultipartFile;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.HotelResponseDto;

import java.util.List;

public interface HotelService {
    HotelResponseDto createHotel(String name,  String state,String location, String description, List<String> amenities, List<MultipartFile> pictures);
    List<HotelDtoFilter> getHotelsByState(String stateName);
    String editHotelById(Long id, HotelRequestDto hotelRequestDto);
    HotelDtoFilter getHotelById(Long id);
    String deleteHotelById(Long id);
    Integer getTotalHotelsInState(String state);

    List<HotelDtoFilter> getMostBookedHotelsByState(String stateName);
}
