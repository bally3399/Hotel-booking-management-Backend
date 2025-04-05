package topg.bimber_user_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.exceptions.AdminExistException;
import topg.bimber_user_service.exceptions.EmailNotFoundException;
import topg.bimber_user_service.exceptions.InvalidDetailsException;
import topg.bimber_user_service.exceptions.UserNotFoundInDb;
import topg.bimber_user_service.mail.MailService;
import topg.bimber_user_service.models.Admin;
import topg.bimber_user_service.models.State;
import topg.bimber_user_service.repository.AdminRepository;
import topg.bimber_user_service.config.JwtUtils;


import java.math.BigDecimal;
import java.util.List;

import static topg.bimber_user_service.utils.ValidationUtils.isValidEmail;
import static topg.bimber_user_service.utils.ValidationUtils.isValidPassword;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ModelMapper modelMapper;
    private final AdminRepository adminRepository;
    private final MailService mailService;
    private final RoomServiceImpl roomServiceImpl;
    private final HotelServiceImpl hotelServiceImpl;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserCreatedDto createAdmin(UserRequestDto userRequestDto) {
        validateFields(userRequestDto.getEmail(), userRequestDto.getPassword());
        doesUserExists(userRequestDto.getEmail());
        Admin admin = modelMapper.map(userRequestDto, Admin.class);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin = adminRepository.save(admin);
        UserCreatedDto response = modelMapper.map(admin, UserCreatedDto.class);
        response.setMessage("Admin registered successfully");
        return response;
    }

    private void validateFields(String email, String password) {
        if (!isValidEmail(email)) throw new InvalidDetailsException("The email you entered is not correct");
        if (!isValidPassword(password))
            throw new InvalidDetailsException("Password must be between 8 and 16 characters long, including at least one uppercase letter, one lowercase letter, one number, and one special character (e.g., @, #, $, %, ^).");
    }

    private void doesUserExists(String email){
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null) throw new AdminExistException(String.format("Admin with email: %s already exits", email));
    }

    @Override
    public UpdateDetailsResponse updateAdmin(UpdateDetailsRequest updateUserRequest) {
        Admin admin = adminRepository.findByEmail(updateUserRequest.getEmail());

        if (admin == null) {
            UpdateDetailsResponse response = new UpdateDetailsResponse();
            response.setMessage("Admin with email " + updateUserRequest.getEmail() + " not found");
            return response;
        }

        admin.setEmail(updateUserRequest.getEmail());
        admin.setPassword(updateUserRequest.getPassword());
        adminRepository.save(admin);

        UpdateDetailsResponse response = new UpdateDetailsResponse();
        response.setMessage("Updated successfully");
        return response;
    }

    @Override
    public UserResponseDto getAdminById(String adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundInDb("User with id " + adminId + " not found"));
        if (!admin.isEnabled()) {
            throw new IllegalStateException("Your account is not activated. Please activate your account.");
        }
        return new UserResponseDto(admin.getEmail(), admin.getUsername(), admin.getId());
    }


    @Override
    public String deleteAdminById(String adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundInDb("User with id " + adminId + " not found"));
        if (!admin.isEnabled()) {
            throw new IllegalStateException("Your account is not activated. Please activate your account.");
        }
        adminRepository.delete(admin);
        return "User with id " + adminId + " has been successfully deleted.";
    }

    @Override
    public void deleteAll() {
        adminRepository.deleteAll();
    }

    @Override
    public RoomResponse addRoom(RoomRequest roomRequest, List<String> multipartFiles) {
        return roomServiceImpl.createRoom(roomRequest, multipartFiles);
    }

    @Override
    public HotelResponseDto createHotel(CreateHotelDto createHotelDto) {
        return hotelServiceImpl.createHotel(createHotelDto);
    }

    @Override
    public Admin findByEmail(String mail) {
        Admin admin = adminRepository.findByEmail(mail);
        if(!admin.getEmail().equals(mail)) throw new EmailNotFoundException("Email Not found");
        return admin;
    }

    @Override
    public List<HotelDtoFilter> getHotelsByState(State state) {
        return hotelServiceImpl.getHotelsByState(String.valueOf(state));
    }

    @Override
    public HotelResponseDto editHotelById(Long id, HotelRequestDto updatedHotelDto) {
        return hotelServiceImpl.editHotelById(id, updatedHotelDto);
    }

    @Override
    public HotelDtoFilter getHotelById(Long id) {
        return hotelServiceImpl.getHotelById(id);
    }

    @Override
    public String deleteHotelById(Long id) {
        return hotelServiceImpl.deleteHotelById(id);
    }

    @Override
    public List<HotelDtoFilter> getHotelsInState(State state) {
        return hotelServiceImpl.getTotalHotelsInState(String.valueOf(state));
    }

    @Override
    public List<RoomResponse> filterByPriceAndState(BigDecimal bigDecimal, BigDecimal bigDecimal1, State state) {
        return roomServiceImpl.filterByPriceAndState(bigDecimal, bigDecimal1, state);
    }

    @Override
    public List<RoomResponse> filterHotelRoomByType(long l, String deluxe) {
        return roomServiceImpl.filterHotelRoomByType(l, deluxe);
    }

    @Override
    public RoomResponse deactivateRoomByHotelId(long hotelId, long roomId) {
        return roomServiceImpl.deactivateRoomByHotelId(hotelId, roomId);
    }

    @Override
    public RoomResponse activateRoomByHotelId(long hotelId, long roomId) {
        return roomServiceImpl.activateRoomByHotelId(hotelId, roomId);
    }

    @Override
    public List<RoomResponse> findAllAvailableHotelRooms(long roomId) {
        return roomServiceImpl.findAllAvailableHotelRooms(roomId);
    }

    @Override
    public boolean isRoomAvailable(long roomId) {
        return roomServiceImpl.isRoomAvailable(roomId);
    }

    @Override
    public List<RoomResponse> findAllRoomsByHotelId(long roomId) {
        return roomServiceImpl.findAllAvailableHotelRooms(roomId);
    }

    @Override
    public String deleteRoomById(long roomId) {
        return roomServiceImpl.deleteRoomById(roomId);
    }

    @Override
    public String editRoomById(long l, RoomRequest roomRequest) {
        return roomServiceImpl.editRoomById(l, roomRequest);
    }

    @Override
    public List<HotelDtoFilter> getMostBookedHotelInState(State state) {
        return hotelServiceImpl.getMostBookedHotelsByState(String.valueOf(state));
    }


}
