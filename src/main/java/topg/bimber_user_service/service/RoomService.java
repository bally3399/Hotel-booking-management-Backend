package topg.bimber_user_service.service;

import org.springframework.web.multipart.MultipartFile;
import topg.bimber_user_service.dto.requests.RoomRequestDto;
import topg.bimber_user_service.dto.responses.RoomResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {
    RoomResponseDto createRoom(RoomRequestDto roomRequestDto, List<MultipartFile> pictures);
    String editRoomById(Long id, RoomRequestDto roomRequestDto);
    String deleteRoomById(Long id);
    List<RoomResponseDto> findAllRoomsByHotelId(Long hotelId);
    boolean isRoomAvailable(Long id);
    List<RoomResponseDto> findAllAvailableHotelRooms(Long hotelId);
    RoomResponseDto deactivateRoomByHotelId(Long hotelId, Long roomId);
    RoomResponseDto activateRoomByHotelId(Long hotelId, Long roomId);
    List<RoomResponseDto> filterHotelRoomByType(Long hotelId, String type);
    List<RoomResponseDto> filterByPriceAndState(BigDecimal minPrice, BigDecimal maxPrice, String state);

}
