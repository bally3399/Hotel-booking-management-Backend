package topg.bimber_user_service.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import topg.bimber_user_service.dto.requests.RoomRequest;
import topg.bimber_user_service.dto.responses.RoomResponse;
import topg.bimber_user_service.exceptions.UserNotFoundInDb;
import topg.bimber_user_service.models.*;
import topg.bimber_user_service.repository.HotelRepository;
import topg.bimber_user_service.repository.RoomPictureRepository;
import topg.bimber_user_service.repository.RoomRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    public RoomResponse createRoom(RoomRequest roomRequest, List<MultipartFile> pictures) {
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        Room room = Room.builder()
                .hotel(hotel)
                .roomType(roomRequest.getRoomType())
                .price(roomRequest.getPrice())
                .available(roomRequest.getAvailable())
                .build();

        Room savedRoom = roomRepository.save(room);

        List<RoomPicture> savedPictures = new ArrayList<>();
        if (pictures != null) {
            for (MultipartFile file : pictures) {
                RoomPicture picture = new RoomPicture();
                picture.setRoom(room);
                picture.setFileName(file.getOriginalFilename());
                picture.setFileType(file.getContentType());
                try {
                    picture.setData(file.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                savedPictures.add(roomPictureRepository.save(picture));
            }
        }

        return new RoomResponse
                (
                savedRoom.getId(),
                savedRoom.getRoomType(),
                savedRoom.getPrice(),
                savedRoom.isAvailable(),
                        savedPictures

                );
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
    public List<RoomResponse> findAllRoomsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        List<Room> rooms = roomRepository.findByHotelId(hotelId);

        return rooms.stream().map(room -> new RoomResponse(
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
    public RoomResponse activateRoomByHotelId(Long hotelId, Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();

            if (!room.getHotel().getId().equals(hotelId)) {
                throw new IllegalArgumentException("Room does not belong to the specified hotel");
            }

            room.setAvailable(true);
            Room updatedRoom = roomRepository.save(room);

            List<RoomPicture> pictures = updatedRoom.getPictures();


            return new RoomResponse(
                    updatedRoom.getId(),
                    updatedRoom.getRoomType(),
                    updatedRoom.getPrice(),
                    updatedRoom.isAvailable(),
                    pictures
            );
        } else {
            throw new NoSuchElementException("Room not found");
        }
    }




//    @Override
//    @Transactional
//    public String editRoomById(Long id, RoomRequest roomRequestDto) {
//        Room room = roomRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundInDb("Room not found"));
//
//        if (roomRequestDto.roomType() != null) {
//            room.setRoomType(roomRequestDto.roomType());
//        }
//        if (roomRequestDto.price() != null) {
//            room.setPrice(roomRequestDto.price());
//        }
//        if (roomRequestDto.available() != null) {
//            room.setAvailable(roomRequestDto.available());
//        }
//
//        room = roomRepository.save(room);
//
//        return "You have successfully updated room: " + room.getId();
//    }
//








//    @Override
//    public List<RoomResponse> findAllAvailableHotelRooms(Long hotelId) {
//        List<Room> availableRooms = roomRepository.findByHotelIdAndAvailable(hotelId, true);
//        return availableRooms.stream()
//                .map(room-> {
//                    List<String> pictureUrls = room.getPictures().stream()
//                            .map(RoomPicture::getFileName)
//                            .toList();
//                    return new RoomResponse(
//                            room.getId(),
//                            room.getRoomType(),
//                            room.getPrice(),
//                            room.isAvailable(),
//                            pictureUrls
//                    );
//                }).collect(Collectors.toList());
//    }


//    @Override
//    public RoomResponse deactivateRoomByHotelId(Long hotelId, Long roomId) {
//        Room room = roomRepository.findByHotelIdAndId(hotelId, roomId)
//                .orElseThrow(() -> new UserNotFoundInDb("Room with ID " + roomId + " not found in hotel with ID " + hotelId));

//        room.setAvailable(false);
//        room = roomRepository.save(room);
//        List<String> pictureUrls = room.getPictures().stream()
//                .map(RoomPicture::getFileName)
//                .toList();
//
//        return new RoomResponse(
//                room.getId(),
//                room.getRoomType(),
//                room.getPrice(),
//                room.isAvailable(),
//                pictureUrls
//        );
//    }



//
//    @Override
//    public RoomResponse activateRoomByHotelId(Long hotelId, Long roomId) {
//        Room room = roomRepository.findByHotelIdAndId(hotelId, roomId)
//                .orElseThrow(() -> new UserNotFoundInDb("Room with ID " + roomId + " not found in hotel with ID " + hotelId));
//
//        room.setAvailable(true);
//        room = roomRepository.save(room);
//        List<String> pictureUrls = room.getPictures().stream()
//                .map(RoomPicture::getFileName)
//                .toList();
//
//        return new RoomResponse(
//                room.getId(),
//                room.getRoomType(),
//                room.getPrice(),
//                room.isAvailable(),
//                pictureUrls
//        );
//    }
//
//
//    @Override
//    public List<RoomResponse> filterHotelRoomByType(Long hotelId, String type) {
//        List<Room> rooms = roomRepository.findByHotelIdAndRoomType(hotelId, RoomType.valueOf(type.toUpperCase()));
//
//        return rooms.stream()
//                .map(room-> {
//                    List<String> pictureUrls = room.getPictures().stream()
//                            .map(RoomPicture::getFileName)
//                            .toList();
//                    return new RoomResponse(
//                            room.getId(),
//                            room.getRoomType(),
//                            room.getPrice(),
//                            room.isAvailable(),
//                            pictureUrls
//                    );
//                }).collect(Collectors.toList());
//    }
//
//
//    @Override
//    public List<RoomResponse> filterByPriceAndState(BigDecimal minPrice, BigDecimal maxPrice, String state) {
//        List<Room> rooms = roomRepository.findByPriceBetweenAndHotel_State(minPrice, maxPrice, State.valueOf(state.toUpperCase()));
//
//
//        return rooms.stream()
//                .map(room-> {
//                    List<String> pictureUrls = room.getPictures().stream()
//                            .map(RoomPicture::getFileName)
//                            .toList();
//                    return new RoomResponse(
//                            room.getId(),
//                            room.getRoomType(),
//                            room.getPrice(),
//                            room.isAvailable(),
//                            pictureUrls
//                    );
//                }).collect(Collectors.toList());
//    }


}
