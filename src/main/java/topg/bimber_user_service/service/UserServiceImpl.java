package topg.bimber_user_service.service;

import io.lettuce.core.AbstractRedisAsyncCommands;
import io.lettuce.core.GeoValue;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.UserCreatedDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.repository.UserRepository;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private UserRepository userRepository;

    public UserServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserCreatedDto createUser(UserRequestDto userRequestDto) {

        User user = modelMapper.map(userRequestDto, User.class);
        user = userRepository.save(user);
        UserCreatedDto response = modelMapper.map(user, UserCreatedDto.class);
        response.setMessage("Registration successful");
        return response;
    }

    @Override
    public UserResponseDto getUserById(String userId) {
        return null;
    }

    @Override
    public UserResponseDto editUserById(UserAndAdminUpdateDto userAndAdminUpdateDto, String userId) {
        return null;
    }

    @Override
    public String deleteUserById(String userId) {
        return "";
    }

    @Override
    public String fundAccount(String userId, BigDecimal amount) {
        return "";
    }

    @Override
    public Long getNumberOfUsers() {
        return 0L;
    }
}