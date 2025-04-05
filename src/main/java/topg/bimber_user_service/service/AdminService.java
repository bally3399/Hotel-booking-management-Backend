package topg.bimber_user_service.service;

import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.Admin;
import topg.bimber_user_service.models.State;

import java.util.List;

public interface AdminService {
    UserCreatedDto createAdmin(UserRequestDto userRequestDto);


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
}
