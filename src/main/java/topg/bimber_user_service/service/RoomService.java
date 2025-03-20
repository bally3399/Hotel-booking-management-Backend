package topg.bimber_user_service.service;

import org.springframework.web.multipart.MultipartFile;
import topg.bimber_user_service.dto.requests.RoomRequest;
import topg.bimber_user_service.dto.responses.RoomResponse;
import topg.bimber_user_service.models.State;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest roomRequest, List<MultipartFile> pictures);
    String editRoomById(Long id, RoomRequest roomRequest);
    String deleteRoomById(Long id);
    List<RoomResponse> findAllRoomsByHotelId(Long hotelId);
    boolean isRoomAvailable(Long id);
    List<RoomResponse> findAllAvailableHotelRooms(Long hotelId);
    RoomResponse deactivateRoomByHotelId(Long hotelId, Long roomId);
    RoomResponse activateRoomByHotelId(Long hotelId, Long roomId);
    List<RoomResponse> filterHotelRoomByType(Long hotelId, String type);
    List<RoomResponse> filterByPriceAndState(BigDecimal minPrice, BigDecimal maxPrice, State state);

}
