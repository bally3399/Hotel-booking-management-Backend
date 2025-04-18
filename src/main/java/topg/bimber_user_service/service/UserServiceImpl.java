package topg.bimber_user_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.requests.UserAndAdminUpdateDto;
import topg.bimber_user_service.dto.requests.UserRequestDto;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.exceptions.UserNotFoundException;
import topg.bimber_user_service.models.Booking;
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
    private BookingService bookingService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoomServiceImpl roomServiceImpl;

    public UserServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserCreatedDto createUser(UserRequestDto userRequestDto) {

        User user = modelMapper.map(userRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user = userRepository.save(user);
        UserCreatedDto response = modelMapper.map(user, UserCreatedDto.class);
        response.setMessage("Registration successful");
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        userRepository.delete(user);
        return "User deleted successfully";
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
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found"));
    }

    @Override
    public List<UserResponseDto> getNumberOfUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().
                map(user -> modelMapper.map(user,UserResponseDto.class)).toList();
    }

    @Override
    public Booking bookRoom(BookingRequestDto bookingRequest) {
        return bookingService.bookRoom(bookingRequest);
    }

    @Override
    public String cancelBooking(Long bookingId, String userId) {
        return bookingService.cancelBooking(bookingId, userId);
    }

    @Override
    public BookingResponseDto updateBooking(Long bookingId, BookingRequestDto updateRequest) {
        return bookingService.updateBooking(bookingId, updateRequest);
    }

    @Override
    public List<BookingResponseDto> listAllBookings() {
        return bookingService.listAllBookings();
    }

    @Override
    public List<NewRoomResponse> findAllRoomsByHotelId(Long hotelId) {
        return roomServiceImpl.findAllRoomsByHotelId(hotelId);
    }

    @Override
    public boolean isRoomAvailable(Long id) {
        return roomServiceImpl.isRoomAvailable(id);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomServiceImpl.getAllRooms();
    }
}