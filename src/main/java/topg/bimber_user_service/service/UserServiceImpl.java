package topg.bimber_user_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.UserCreatedDto;
import topg.bimber_user_service.dto.responses.UserResponseDto;
import topg.bimber_user_service.exceptions.UserNotFoundException;
import topg.bimber_user_service.models.User;
import topg.bimber_user_service.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserCreatedDto createUser(UserRequestDto userRequestDto) {

        User user = modelMapper.map(userRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        UserCreatedDto response = modelMapper.map(user, UserCreatedDto.class);
        response.setMessage("Registration successful");
        response.setSuccess(true);
        return response;
    }

    @Override
    public UserResponseDto getUserById(String userId) {
       User  user = findById(userId);
       return modelMapper.map(user,UserResponseDto.class);
    }

    @Override
    public UserResponseDto editUserById(UserAndAdminUpdateDto userAndAdminUpdateDto, String userId) {
        User user =  findById(userId);
        if(userAndAdminUpdateDto.email() != null && !userAndAdminUpdateDto.email().isBlank()) user.setEmail(userAndAdminUpdateDto.email());
        if(userAndAdminUpdateDto.username() != null && !userAndAdminUpdateDto.username().isBlank()) user.setUsername(userAndAdminUpdateDto.username());

        return modelMapper.map(user,UserResponseDto.class);
    }

    @Override
    public String deleteUserById(String userId) {
        findById(userId);
        userRepository.deleteById(userId);
        return "User Deleted";
    }

    @Override
    public String fundAccount(String userId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO)>1) throw new IllegalArgumentException("Invalid Amount");
        User user = findById(userId);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        return "Funds Added";
    }

    @Override
    public User findById(String id) {
        User user =  userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found"));
        return user;
    }

    @Override
    public List<UserResponseDto> getNumberOfUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().
                map(user -> modelMapper.map(user,UserResponseDto.class)).toList();
    }
}