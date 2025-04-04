package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.Admin;
import topg.bimber_user_service.models.State;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {
    UserCreatedDto createAdmin(UserRequestDto userRequestDto);

    LoginResponse login(LoginRequest loginRequest);

    UpdateDetailsResponse updateAdmin(UpdateDetailsRequest updateUserRequest);

    UserResponseDto getAdminById(String adminId);
//    UserResponseDto editAdminById(UserAndAdminUpdateDto adminUpdateRequestDto, String adminId);
    String deleteAdminById(String adminId);

    void deleteAll();

    RoomResponse addRoom(RoomRequest roomRequest, List<String> multipartFiles);

    HotelResponseDto createHotel(CreateHotelDto createHotelDto);

    Admin findByEmail(String mail);

    List<HotelDtoFilter> getHotelsByState(State state);

    HotelResponseDto editHotelById(Long id, HotelRequestDto updatedHotelDto);

    HotelDtoFilter getHotelById(Long id);

    String deleteHotelById(Long id);

    List<HotelDtoFilter> getMostBookedHotelInState(State state);

    List<HotelDtoFilter> getHotelsInState(State state);

    List<RoomResponse> filterByPriceAndState(BigDecimal bigDecimal, BigDecimal bigDecimal1, State state);

    List<RoomResponse> filterHotelRoomByType(long l, String deluxe);

    RoomResponse deactivateRoomByHotelId(long hotelId, long roomId);

    RoomResponse activateRoomByHotelId(long hotelId, long roomId);

    List<RoomResponse> findAllAvailableHotelRooms(long roomId);

    boolean isRoomAvailable(long roomId);

    List<RoomResponse> findAllRoomsByHotelId(long roomId);

    String deleteRoomById(long roomId);

    String editRoomById(long l, RoomRequest roomRequest);
}
