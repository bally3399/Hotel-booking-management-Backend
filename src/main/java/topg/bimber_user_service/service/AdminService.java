package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.Admin;
import topg.bimber_user_service.models.Location;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {
    UserCreatedDto createAdmin(UserRequestDto userRequestDto);


    UpdateDetailsResponse updateAdmin(UpdateDetailsRequest updateUserRequest);

    UserResponseDto getAdminById(String adminId);
//    UserResponseDto editAdminById(UserAndAdminUpdateDto adminUpdateRequestDto, String adminId);
    String deleteAdminById(String adminId);

    void deleteAll();
    HotelDtoFilter findByName(String name);
    List<HotelDtoFilter> getAllHotels();

    RoomResponse addRoom(RoomRequest roomRequest);

    HotelResponseDto createHotel(CreateHotelDto createHotelDto);

    Admin findByEmail(String mail);

    List<HotelDtoFilter> getHotelsByLocation(Location location);

    HotelResponseDto editHotelById(Long id, HotelRequestDto updatedHotelDto);

    HotelDtoFilter getHotelById(Long id);

    String deleteHotelById(Long id);

    List<HotelDtoFilter> getMostBookedHotelByLocation(Location location);

    List<HotelDtoFilter> getTotalHotelsByLocation(Location location);


    List<RoomResponse> filterByPriceAndLocation(BigDecimal bigDecimal, BigDecimal bigDecimal1, Location location);

    List<RoomResponse> filterHotelRoomByType(long l, String deluxe);

    RoomResponse deactivateRoomByHotelId(long hotelId, long roomId);

    RoomResponse activateRoomByHotelId(long hotelId, long roomId);

    List<RoomResponse> findAllAvailableHotelRooms(long roomId);

    boolean isRoomAvailable(long roomId);

    List<RoomResponse> findAllRoomsByHotelId(long roomId);

    String deleteRoomById(long roomId);

    String editRoomById(long l, RoomRequest roomRequest);

    List<NewRoomResponse> findAllRoomsByHotelId(Long hotelId);
    List<RoomResponse> getAllRooms ();
}
