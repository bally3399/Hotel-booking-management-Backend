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
import topg.bimber_user_service.models.Location;
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
        HotelDto hotelDto = new HotelDto(hotel.getId(), hotel.getName(),  hotel.getLocation(),  hotel.getAmenities(), hotel.getDescription(), hotel.getPictures());
        return new HotelResponseDto(true, hotelDto);
    }

    @Override
    public List<HotelDtoFilter> getHotelsByLocation(Location location) {
        List<Hotel> hotels = hotelRepository.findByLocation(location);

        if (hotels.isEmpty()) {
            return new ArrayList<>();
        }

        return hotels.stream()
                .map(hotel -> {
                    List<String> pictureUrls = hotel.getPictures();

                    return new HotelDtoFilter(
                            hotel.getId(),
                            hotel.getName(),
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

        if (hotelRequestDto.getLocation() != null) {
            hotel.setLocation(hotelRequestDto.getLocation());
        }

        if (hotelRequestDto.getLocation() != null) {
            hotel.setLocation(hotelRequestDto.getLocation());
        }
        hotel = hotelRepository.save(hotel);
        HotelDto hotelDto = new HotelDto(hotel.getId(), hotel.getName(), hotel.getLocation(), hotel.getAmenities(), hotel.getDescription(), hotel.getPictures());
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
                hotel.getLocation(),
                hotel.getAmenities(),
                hotel.getDescription(),
                pictures
        );

    }

    @Override
    public HotelDtoFilter findByName(String name) {
        Hotel hotel = hotelRepository.findByName(name).orElseThrow(()->new IllegalStateException("Hotel With Name Not Found"));
        List<String> pictureUrls = hotel.getPictures();
        return new HotelDtoFilter(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getAmenities(),
                hotel.getDescription(),
                pictureUrls
        );
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

        hotelRepository.deleteById(id);
        return hotel.getName() + " located at " + hotel.getLocation() + " has been deleted";
    }

    @Override
    public List<HotelDtoFilter> getTotalHotelsByLocation(Location location) {
        return getHotelsByLocation(location);
    }

    @Override
    public List<HotelDtoFilter> getMostBookedHotelsByLocation(Location location) {

        if (location == null) {
            throw new InvalidStateException("State cannot be empty.");
        }


        List<Hotel> hotels = hotelRepository.findMostBookedHotelsByLocation(location);

        return hotels.stream()
                .map(hotel -> {
                    List<String> pictureUrls = hotel.getPictures();
                    return new HotelDtoFilter(
                            hotel.getId(),
                            hotel.getName(),
                            hotel.getLocation(),
                            hotel.getAmenities(),
                            hotel.getDescription(),
                            pictureUrls
                    );
                })
                .collect(Collectors.toList());
    }


}