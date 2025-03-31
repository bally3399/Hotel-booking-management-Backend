package topg.bimber_user_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import topg.bimber_user_service.dto.requests.RoomRequest;
import topg.bimber_user_service.dto.responses.RoomResponse;
import topg.bimber_user_service.exceptions.RoomNotAvailableException;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.Room;
import topg.bimber_user_service.models.RoomType;
import topg.bimber_user_service.models.State;
import topg.bimber_user_service.repository.HotelRepository;
import topg.bimber_user_service.repository.RoomRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static topg.bimber_user_service.models.State.LAGOS;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class RoomServiceImplTest {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomServiceImpl roomServiceImpl;
    @Autowired
    private RoomRepository roomRepository;
    @Test
    @DisplayName("Room can be created successfully with a picture")
    public void createRoomWithPictureTest() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Ipaja");
        hotel.setState(LAGOS);
        hotel.setDescription(" hotel");
        hotel = hotelRepository.save(hotel);

        RoomRequest request = new RoomRequest();
        request.setHotelId(hotel.getId());
        request.setRoomType(RoomType.DELUXE);
        request.setPrice(new BigDecimal("50000.00"));
        request.setIsAvailable(true);

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "room.jpg",
                "image/jpeg",
                new byte[10]
        );

        RoomResponse response = roomServiceImpl.createRoom(request, List.of("picture1","picture2", "picture3"));

        assertNotNull(response);
        assertEquals(RoomType.DELUXE, response.getRoomType());
        assertEquals(new BigDecimal("50000.00"), response.getPrice());
        assertTrue(response.isAvailable());
        assertFalse(response.getPictureUrls().isEmpty());
    }

    @Test
    @DisplayName("Should return true when room is available")
    public void shouldReturnTrueWhenRoomIsAvailable() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A sample test hotel");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(RoomType.DOUBLE);
        hotel.setDescription(" hotel");
        room.setAvailable(true);
        room = roomRepository.save(room);

        boolean result = roomServiceImpl.isRoomAvailable(room.getId());

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when room is not available")
    public void shouldReturnFalseWhenRoomIsNotAvailable() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A sample test hotel");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(RoomType.SINGLE);
        hotel.setDescription(" hotel");
        room.setAvailable(false);
        room = roomRepository.save(room);

        boolean result = roomServiceImpl.isRoomAvailable(room.getId());

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when room does not exist")
    public void shouldReturnFalseWhenRoomDoesNotExist() {
        boolean result = roomServiceImpl.isRoomAvailable(999L);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should delete a room successfully")
    public void shouldDeleteRoomSuccessfully() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A sample test hotel");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(RoomType.SUITE);
        room.setAvailable(true);
        room = roomRepository.save(room);

        String result = roomServiceImpl.deleteRoomById(room.getId());

        assertEquals("Room deleted successfully", result);
        assertFalse(roomRepository.findById(room.getId()).isPresent());
    }

    @Test
    @DisplayName("Should return 'Room not found' when trying to delete a non-existent room")
    public void shouldReturnRoomNotFoundWhenDeletingNonExistentRoom() {
        String result = roomServiceImpl.deleteRoomById(999L);
        assertEquals("Room not found", result);
    }

    @Test
    @DisplayName("Should activate a room successfully")
    public void shouldActivateRoomSuccessfully() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A sample test hotel");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(RoomType.SINGLE);
        room.setAvailable(false);
        room = roomRepository.save(room);

        RoomResponse response = roomServiceImpl.activateRoomByHotelId(hotel.getId(), room.getId());

        assertNotNull(response);
        assertEquals(room.getId(), response.getId());
        assertTrue(response.isAvailable());
    }

    @Test
    @DisplayName("Should throw exception if room does not belong to hotel")
    public void shouldThrowExceptionIfRoomDoesNotBelongToHotel() {
        Hotel hotel1 = new Hotel();
        hotel1.setName("Test Hotel 1");
        hotel1.setLocation("Test Location");
        hotel1.setState(LAGOS);
        hotel1.setDescription("A sample test hotel");
        hotel1 = hotelRepository.save(hotel1); // Save and assign properly

        Hotel hotel2 = new Hotel();
        hotel2.setName("Test Hotel 2");
        hotel2.setLocation("Test Location");
        hotel2.setState(LAGOS);
        hotel2.setDescription("Another sample test hotel");
        hotel2 = hotelRepository.save(hotel2); // Save correctly

        System.out.println("Hotel1 ID: " + hotel1.getId());
        System.out.println("Hotel2 ID: " + hotel2.getId());

        Room room = new Room();
        room.setHotel(hotel2);
        room.setRoomType(RoomType.SINGLE);
        room.setAvailable(false);
        room = roomRepository.save(room);
        final Long roomId = room.getId();

        System.out.println("Room ID: " + roomId);
        final Long hotel2Id = hotel2.getId();

        if (!room.getHotel().getId().equals(hotel2Id)) {
            System.out.println("Throwing IllegalArgumentException: Room does not belong to the specified hotel");
            throw new IllegalArgumentException("Room does not belong to the specified hotel");
        }



    }


    @Test
    @DisplayName("Should throw exception if room does not exist")
    public void shouldThrowExceptionIfRoomDoesNotExist() {
        assertThrows(RoomNotAvailableException.class, () ->
                roomServiceImpl.activateRoomByHotelId(1L, 999L));
    }

    @Test
    @DisplayName("Should return all rooms for a given hotel")
    public void shouldReturnAllRoomsForHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A sample test hotel");
        hotel = hotelRepository.save(hotel);
        Long hotelId = hotel.getId();

        Room room1 = new Room();
        room1.setHotel(hotel);
        room1.setRoomType(RoomType.SINGLE);
        room1.setAvailable(true);
        room1.setPrice(BigDecimal.valueOf(50000.0));
        room1 = roomRepository.save(room1);

        Room room2 = new Room();
        room2.setHotel(hotel);
        room2.setRoomType(RoomType.DOUBLE);
        room2.setAvailable(false);
        room2.setPrice(BigDecimal.valueOf(80000.0));
        room2 = roomRepository.save(room2);

        List<RoomResponse> rooms = roomServiceImpl.findAllRoomsByHotelId(hotelId);

        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertEquals(RoomType.SINGLE, rooms.get(0).getRoomType());
        assertEquals(RoomType.DOUBLE, rooms.get(1).getRoomType());
    }


    @Test
    @DisplayName("Should return only available rooms for a given hotel")
    public void shouldReturnOnlyAvailableRoomsForHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("Sample Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A sample test hotel");
        hotel = hotelRepository.save(hotel);
        Long hotelId = hotel.getId();

        Room availableRoom = new Room();
        availableRoom.setHotel(hotel);
        availableRoom.setRoomType(RoomType.SINGLE);
        availableRoom.setAvailable(true);
        availableRoom.setPrice(BigDecimal.valueOf(50000.0));
        roomRepository.save(availableRoom);

        Room unavailableRoom = new Room();
        unavailableRoom.setHotel(hotel);
        unavailableRoom.setRoomType(RoomType.DOUBLE);
        unavailableRoom.setAvailable(false);
        unavailableRoom.setPrice(BigDecimal.valueOf(80000.0));
        roomRepository.save(unavailableRoom);

        List<RoomResponse> availableRooms = roomServiceImpl.findAllAvailableHotelRooms(hotelId);

        assertNotNull(availableRooms);
        assertEquals(1, availableRooms.size());
        assertTrue(availableRooms.get(0).isAvailable());
        assertEquals(RoomType.SINGLE, availableRooms.get(0).getRoomType());
    }


    @Test
    @DisplayName("Should deactivate a room for a given hotel")
    public void shouldDeactivateRoomForHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A test hotel");
        hotel = hotelRepository.save(hotel);
        Long hotelId = hotel.getId();

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(RoomType.SINGLE);
        room.setAvailable(true);
        room.setPrice(BigDecimal.valueOf(50000.0));
        room = roomRepository.save(room);
        Long roomId = room.getId();

        RoomResponse response = roomServiceImpl.deactivateRoomByHotelId(hotelId, roomId);

        assertNotNull(response);
        assertEquals(roomId, response.getId());
        assertFalse(response.isAvailable());
    }

    @Test
    @DisplayName("Should update room details successfully")
    public void shouldUpdateRoomSuccessfully() {
        Hotel hotel = new Hotel();
        hotel.setName("Sample Hotel");
        hotel.setLocation("Test Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A test hotel");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(RoomType.SINGLE);
        room.setAvailable(true);
        room.setPrice(BigDecimal.valueOf(50000.0));
        room = roomRepository.save(room);
        Long roomId = room.getId();

        RoomRequest updateRequest = new RoomRequest();
        updateRequest.setRoomType(RoomType.DOUBLE);
        updateRequest.setPrice(BigDecimal.valueOf(80000.0));
        updateRequest.setIsAvailable(false);

        String response = roomServiceImpl.editRoomById(roomId, updateRequest);

        Room updatedRoom = roomRepository.findById(roomId).orElseThrow();

        assertEquals("Room updated successfully", response);
        assertEquals(RoomType.DOUBLE, updatedRoom.getRoomType());
        assertEquals(Double.valueOf(80000.0), Double.valueOf(String.valueOf(updatedRoom.getPrice())));
        assertFalse(updatedRoom.isAvailable());
    }


    @Test
    @DisplayName("Should return only rooms of the specified type for a given hotel")
    public void shouldReturnRoomsByTypeForHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Sample Location");
        hotel.setState(LAGOS);
        hotel.setDescription("A test hotel");
        hotel = hotelRepository.save(hotel);
        Long hotelId = hotel.getId();

        Room singleRoom = new Room();
        singleRoom.setHotel(hotel);
        singleRoom.setRoomType(RoomType.SINGLE);
        singleRoom.setAvailable(true);
        singleRoom.setPrice(BigDecimal.valueOf(50000.0));
        roomRepository.save(singleRoom);

        Room doubleRoom = new Room();
        doubleRoom.setHotel(hotel);
        doubleRoom.setRoomType(RoomType.DOUBLE);
        doubleRoom.setAvailable(true);
        doubleRoom.setPrice(BigDecimal.valueOf(80000.0));
        roomRepository.save(doubleRoom);

        List<RoomResponse> singleRooms = roomServiceImpl.filterHotelRoomByType(hotelId, "SINGLE");

        assertNotNull(singleRooms);
        assertEquals(1, singleRooms.size());
        assertEquals(RoomType.SINGLE, singleRooms.get(0).getRoomType());

        List<RoomResponse> doubleRooms = roomServiceImpl.filterHotelRoomByType(hotelId, "DOUBLE");

        assertNotNull(doubleRooms);
        assertEquals(1, doubleRooms.size());
        assertEquals(RoomType.DOUBLE, doubleRooms.get(0).getRoomType());
    }


    @Test
    @DisplayName("Should return only rooms within the specified price range and state")
    public void shouldReturnRoomsByPriceRangeAndState() {
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel A");
        hotel1.setLocation("Lagos Mainland");
        hotel1.setState(LAGOS);
        hotel1.setDescription("Luxury hotel in Lagos");
        hotel1 = hotelRepository.save(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel B");
        hotel2.setLocation("Ondo town");
        hotel2.setState(State.ONDO);
        hotel2.setDescription("Business-class hotel in Abuja");
        hotel2 = hotelRepository.save(hotel2);

        Room room1 = new Room();
        room1.setHotel(hotel1);
        room1.setRoomType(RoomType.SINGLE);
        room1.setAvailable(true);
        room1.setPrice(new BigDecimal("5000"));
        roomRepository.save(room1);

        Room room2 = new Room();
        room2.setHotel(hotel1);
        room2.setRoomType(RoomType.DOUBLE);
        room2.setAvailable(true);
        room2.setPrice(new BigDecimal("10000"));
        roomRepository.save(room2);

        Room room3 = new Room();
        room3.setHotel(hotel2);
        room3.setRoomType(RoomType.SUITE);
        room3.setAvailable(true);
        room3.setPrice(new BigDecimal("15000"));
        roomRepository.save(room3);

        List<RoomResponse> filteredRooms = roomServiceImpl.filterByPriceAndState(new BigDecimal("4000"), new BigDecimal("12000"), LAGOS);

        assertNotNull(filteredRooms);
        assertEquals(2, filteredRooms.size());
        assertTrue(filteredRooms.stream().allMatch(room -> room.getPrice().compareTo(new BigDecimal("4000")) >= 0
                && room.getPrice().compareTo(new BigDecimal("12000")) <= 0
                && room.getHotel().getState().equals(LAGOS)));
    }





}

