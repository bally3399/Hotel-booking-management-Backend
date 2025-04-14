package topg.bimber_user_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import topg.bimber_user_service.dto.requests.CreateHotelDto;
import topg.bimber_user_service.dto.requests.HotelDto;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.HotelResponseDto;
import topg.bimber_user_service.exceptions.InvalidStateException;
import topg.bimber_user_service.exceptions.InvalidUserInputException;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.State;
import topg.bimber_user_service.repository.HotelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;



    @Transactional
    @Override
    public HotelResponseDto createHotel(CreateHotelDto createHotelDto) {
        Hotel hotel = modelMapper.map(createHotelDto,Hotel.class);
        hotel = hotelRepository.save(hotel);
        HotelDto hotelDto = new HotelDto(hotel.getId(), hotel.getName(),  hotel.getState(), hotel.getLocation(), hotel.getAmenities(), hotel.getDescription(), hotel.getPictures());
        return new HotelResponseDto(true, hotelDto);
    }

    @Override
    public List<HotelDtoFilter> getHotelsByState(String stateName) {
        State state = State.valueOf(stateName.toUpperCase());
        List<Hotel> hotels = hotelRepository.findByState(state);

        if (hotels.isEmpty()) {
            return new ArrayList<>();
        }

        return hotels.stream()
                .map(hotel -> {
                    List<String> pictureUrls = hotel.getPictures();

                    return new HotelDtoFilter(
                            hotel.getId(),
                            hotel.getName(),
                            hotel.getState(),
                            hotel.getLocation(),
                            hotel.getAmenities(),
                            hotel.getDescription(),
                            pictureUrls
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public HotelResponseDto editHotelById(Long id, HotelRequestDto hotelRequestDto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new InvalidUserInputException("Id not found"));

        if (hotelRequestDto.getName() != null &&
                !hotel.getName().equals(hotelRequestDto.getName()) &&
                hotelRepository.findByName(hotelRequestDto.getName()).isPresent()) {
            throw new InvalidUserInputException("Name is already taken");
        }
        if (hotelRequestDto.getName() != null) {
            hotel.setName(hotelRequestDto.getName());
        }
        if (hotelRequestDto.getDescription() != null) {
            hotel.setDescription(hotelRequestDto.getDescription());
        }

        if (hotelRequestDto.getState() != null) {
            hotel.setState(hotelRequestDto.getState());
        }

        if (hotelRequestDto.getLocation() != null) {
            hotel.setLocation(hotelRequestDto.getLocation());
        }
        hotel = hotelRepository.save(hotel);
        HotelDto hotelDto = new HotelDto(hotel.getId(), hotel.getName(), hotel.getState(), hotel.getLocation(), hotel.getAmenities(), hotel.getDescription(), hotel.getPictures());
        return new HotelResponseDto(true, hotelDto);
    }

    @Override
    public HotelDtoFilter getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new InvalidUserInputException("Id not found"));
        List<String> pictures = hotel.getPictures();
        return new HotelDtoFilter(
                hotel.getId(),
                hotel.getName(),
                hotel.getState(),
                hotel.getLocation(),
                hotel.getAmenities(),
                hotel.getDescription(),
                pictures
        );

    }

    @Override
    public HotelDtoFilter findByName(String name) {
        Hotel hotel = hotelRepository.findByName(name).orElseThrow(()->new IllegalStateException("Hotel With Name Not Found"));
        return modelMapper.map(hotel,HotelDtoFilter.class);
    }

    @Override
    public List<HotelDtoFilter> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return  hotels.stream()
                .map(hotel -> {
                    List<String> pictureUrls = hotel.getPictures();
                    return new HotelDtoFilter(
                            hotel.getId(),
                            hotel.getName(),
                            hotel.getState(),
                            hotel.getLocation(),
                            hotel.getAmenities(),
                            hotel.getDescription(),
                            pictureUrls
                    );
                })
                .collect(Collectors.toList());
    }


    @Override
    public String deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new InvalidUserInputException("Id not found"));

        hotelRepository.delete(hotel);
        return hotel.getName() + " located at " + hotel.getState() + " has been deleted";
    }

    @Override
    public List<HotelDtoFilter> getTotalHotelsInState(String state) {
        return getHotelsByState(state);
    }

    @Override
    public List<HotelDtoFilter> getMostBookedHotelsByState(String stateName) {

        if (stateName == null || stateName.trim().isEmpty()) {
            throw new InvalidStateException("State cannot be empty.");
        }

        State state = State.valueOf(stateName.toUpperCase());

        List<Hotel> hotels = hotelRepository.findMostBookedHotelsByState(state);

        return hotels.stream()
                .map(hotel -> {
                    List<String> pictureUrls = hotel.getPictures();
                    return new HotelDtoFilter(
                            hotel.getId(),
                            hotel.getName(),
                            hotel.getState(),
                            hotel.getLocation(),
                            hotel.getAmenities(),
                            hotel.getDescription(),
                            pictureUrls
                    );
                })
                .collect(Collectors.toList());
    }


}