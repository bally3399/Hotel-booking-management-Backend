package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.RoomRequest;
import topg.bimber_user_service.dto.responses.DeleteRoomResponse;
import topg.bimber_user_service.dto.responses.EditRoomResponse;
import topg.bimber_user_service.dto.responses.NewRoomResponse;
import topg.bimber_user_service.dto.responses.RoomResponse;
import topg.bimber_user_service.models.Location;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest roomRequest);
    RoomResponse getRoom(Long id);
    EditRoomResponse editRoomById(Long id, RoomRequest roomRequest);
    DeleteRoomResponse deleteRoomById(Long id);
    List<NewRoomResponse> findAllRoomsByHotelId(Long hotelId);
    boolean isRoomAvailable(Long id);
    List<RoomResponse> getAllRooms ();
    List<RoomResponse> findAllAvailableHotelRooms(Long hotelId);
    RoomResponse deactivateRoomByHotelId(Long hotelId, Long roomId);
    RoomResponse activateRoomByHotelId(Long hotelId, Long roomId);
    List<RoomResponse> filterHotelRoomByType(Long hotelId, String type);
    List<RoomResponse> filterByPriceAndLocation(BigDecimal minPrice, BigDecimal maxPrice, Location location);

    List<NewRoomResponse> getAllAvailableRooms();
}
