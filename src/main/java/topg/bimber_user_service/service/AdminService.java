package topg.bimber_user_service.service;

import ch.qos.logback.classic.spi.EventArgUtil;
import org.springframework.web.multipart.MultipartFile;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;

import java.util.List;

public interface AdminService {
    UserCreatedDto createAdmin(UserRequestDto userRequestDto);

    LoginResponse login(LoginRequest loginRequest);

    UserResponseDto getAdminById(String adminId);
//    UserResponseDto editAdminById(UserAndAdminUpdateDto adminUpdateRequestDto, String adminId);
    String deleteAdminById(String adminId);

    void deleteAll();

    RoomResponse addRoom(RoomRequest roomRequest, List<String> multipartFiles);

    HotelResponseDto createHotel(CreateHotelDto createHotelDto);
}
