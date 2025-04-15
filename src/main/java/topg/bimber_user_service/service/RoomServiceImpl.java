package topg.bimber_user_service.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import topg.bimber_user_service.dto.requests.RoomRequest;
import topg.bimber_user_service.dto.responses.NewRoomResponse;
import topg.bimber_user_service.dto.responses.RoomResponse;
import topg.bimber_user_service.exceptions.RoomNotAvailableException;
import topg.bimber_user_service.models.*;
import topg.bimber_user_service.repository.HotelRepository;
import topg.bimber_user_service.repository.RoomPictureRepository;
import topg.bimber_user_service.repository.RoomRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomPictureRepository roomPictureRepository;

    public RoomServiceImpl(RoomRepository roomRepository, HotelRepository hotelRepository, RoomPictureRepository roomPictureRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.roomPictureRepository = roomPictureRepository;
    }

    @Transactional
    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        Room room = Room.builder()
                .hotel(hotel)
                .roomType(roomRequest.getRoomType())
                .price(roomRequest.getPrice())
                .available(roomRequest.getIsAvailable())
                .pictures(roomRequest.getPictures())
                .build();

        Room savedRoom = roomRepository.save(room);

        return new RoomResponse
                (
                        savedRoom.getId(),
                        savedRoom.getRoomType(),
                        savedRoom.getPrice(),
                        savedRoom.isAvailable(),
                        savedRoom.getPictures()
                );
    }

    @Override
    public String editRoomById(Long id, RoomRequest roomRequest) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotAvailableException("Room not found"));

        room.setRoomType(roomRequest.getRoomType());
        room.setPrice(roomRequest.getPrice());
        room.setAvailable(roomRequest.getIsAvailable());

        roomRepository.save(room);

        return "Room updated successfully";
    }


    @Override
    public String deleteRoomById(Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            roomRepository.deleteById(id);
            return "Room deleted successfully";
        } else {
            return "Room not found";
        }
    }

    @Override
    public List<NewRoomResponse> findAllRoomsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        List<Room> rooms = roomRepository.findByHotelId(hotelId);

        return rooms.stream().map(room -> new NewRoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getPrice(),
                room.isAvailable(),
                room.getPictures()
        )).collect(Collectors.toList());
    }


    @Override
    public boolean isRoomAvailable(Long id) {
        return roomRepository.findById(id)
                .map(Room::isAvailable)
                .orElse(false);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream().map(room -> new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getPrice(),
                room.isAvailable(),
                room.getPictures()
        )).collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> findAllAvailableHotelRooms(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        List<Room> availableRooms = roomRepository.findByHotelIdAndAvailable(hotelId, true);

        return availableRooms.stream().map(room -> new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getPrice(),
                room.isAvailable(),
                room.getPictures()
        )).collect(Collectors.toList());
    }

    @Override
    public RoomResponse deactivateRoomByHotelId(Long hotelId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("Room not found"));

        if (!room.getHotel().getId().equals(hotelId)) {
            throw new IllegalArgumentException("Room does not belong to the specified hotel");
        }

        room.setAvailable(false);
        Room updatedRoom = roomRepository.save(room);

        return new RoomResponse(
                updatedRoom.getId(),
                updatedRoom.getRoomType(),
                updatedRoom.getPrice(),
                updatedRoom.isAvailable(),
                updatedRoom.getPictures()
        );
    }


    @Override
    public RoomResponse activateRoomByHotelId(Long hotelId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotAvailableException("Room not found"));

        if (!room.getHotel().getId().equals(hotelId)) {
            throw new IllegalArgumentException("Room does not belong to the specified hotel");
        }

        room.setAvailable(true);
        Room updatedRoom = roomRepository.save(room);

        return new RoomResponse(
                updatedRoom.getId(),
                updatedRoom.getRoomType(),
                updatedRoom.getPrice(),
                updatedRoom.isAvailable(),
                updatedRoom.getPictures()
        );
    }

    @Override
    public List<RoomResponse> filterHotelRoomByType(Long hotelId, String type) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        RoomType roomType;
        try {
            roomType = RoomType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room type");
        }

        List<Room> filteredRooms = roomRepository.findByHotelIdAndRoomType(hotelId, roomType);

        return filteredRooms.stream().map(room -> new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getPrice(),
                room.isAvailable(),
                room.getPictures()
        )).collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> filterByPriceAndLocation(BigDecimal minPrice, BigDecimal maxPrice, Location location) {
        List<Room> rooms = roomRepository.findByPriceBetweenAndHotelLocation(minPrice, maxPrice, location);

        return rooms.stream().map(room -> new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getPrice(),
                room.isAvailable(),
                room.getPictures()
        )).collect(Collectors.toList());
    }



}