package topg.bimber_user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.RoomRequest;
import topg.bimber_user_service.dto.responses.DeleteRoomResponse;
import topg.bimber_user_service.dto.responses.EditRoomResponse;
import topg.bimber_user_service.dto.responses.NewRoomResponse;
import topg.bimber_user_service.dto.responses.RoomResponse;
import topg.bimber_user_service.models.Location;
import topg.bimber_user_service.service.RoomServiceImpl;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomServiceImpl roomServiceImpl;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRoom(
            @RequestPart("roomRequest") @Valid RoomRequest roomRequest,
            @RequestPart(value = "pictures", required = false) List<String> pictures) {

        RoomResponse roomResponse = roomServiceImpl.createRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EditRoomResponse> editRoomById(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        EditRoomResponse response = roomServiceImpl.editRoomById(id, roomRequest);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteRoomResponse> deleteRoomById(@PathVariable Long id) {
        DeleteRoomResponse response = roomServiceImpl.deleteRoomById(id);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> findAllRoomsByHotelId(@PathVariable Long hotelId) {
        List<NewRoomResponse> rooms = roomServiceImpl.findAllRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }
    @GetMapping("/")
    public ResponseEntity<?> findAllRoomsByHotelId() {
        List<RoomResponse> rooms = roomServiceImpl.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<?> findAllAvailableHotelRooms(@PathVariable Long hotelId) {
        List<RoomResponse> availableRooms = roomServiceImpl.findAllAvailableHotelRooms(hotelId);
        return ResponseEntity.ok(availableRooms);
    }

    @PutMapping("/hotel/{hotelId}/room/{roomId}/deactivate")
    public ResponseEntity<?> deactivateRoom(
            @PathVariable Long hotelId,
            @PathVariable Long roomId
    ) {
        RoomResponse response = roomServiceImpl.deactivateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/hotel/{hotelId}/room/{roomId}/activate")
    public ResponseEntity<?> activateRoom(
            @PathVariable Long hotelId,
            @PathVariable Long roomId
    ) {
        RoomResponse response = roomServiceImpl.activateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/hotel/{hotelId}/filter")
    public ResponseEntity<?> filterRoomsByType(
            @PathVariable Long hotelId,
            @RequestParam String type
    ) {
        List<RoomResponse> response = roomServiceImpl.filterHotelRoomByType(hotelId, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterRoomsByPriceAndLocation(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam Location location
    ) {
        List<RoomResponse> response = roomServiceImpl.filterByPriceAndLocation(minPrice, maxPrice, location);
        return ResponseEntity.ok(response);
    }


}