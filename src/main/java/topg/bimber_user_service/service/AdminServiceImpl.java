package topg.bimber_user_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.exceptions.AdminExistException;
import topg.bimber_user_service.exceptions.EmailNotFoundException;
import topg.bimber_user_service.exceptions.InvalidDetailsException;
import topg.bimber_user_service.exceptions.UserNotFoundInDb;
import topg.bimber_user_service.mail.MailService;
import topg.bimber_user_service.models.Admin;
import topg.bimber_user_service.repository.AdminRepository;
import topg.bimber_user_service.utils.JwtUtils;


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

    @Override
    public UserCreatedDto createAdmin(UserRequestDto userRequestDto) {
        validateFields(userRequestDto.getEmail(), userRequestDto.getPassword());
        doesUserExists(userRequestDto.getEmail());
        Admin admin = modelMapper.map(userRequestDto, Admin.class);
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
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return checkLoginDetail(email, password);
    }


    private LoginResponse checkLoginDetail(String email, String password) {
        Admin optionalUser = adminRepository.findByEmail(email);
        if (optionalUser != null){
            if (optionalUser.getPassword().equals(password)) {
                return loginResponseMapper(optionalUser);
            } else {
                throw new InvalidDetailsException("Invalid username or password");
            }
        } else {
            throw new InvalidDetailsException("Invalid username or password");
        }
    }

    private LoginResponse loginResponseMapper(Admin admin) {
        LoginResponse loginResponse = new LoginResponse();
        String accessToken = JwtUtils.generateAccessToken(admin.getId());
        BeanUtils.copyProperties(admin, loginResponse);
        loginResponse.setJwtToken(accessToken);
        loginResponse.setMessage("Login Successful");
        loginResponse.setRole(admin.getRole());
        return loginResponse;
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



}
