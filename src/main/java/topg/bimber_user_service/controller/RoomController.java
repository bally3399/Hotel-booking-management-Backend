package topg.bimber_user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.RoomRequest;
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
    public ResponseEntity<RoomResponse> createRoom(
            @RequestPart("roomRequest") @Valid RoomRequest roomRequest,
            @RequestPart(value = "pictures", required = false) List<String> pictures) {

        RoomResponse roomResponse = roomServiceImpl.createRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> editRoomById(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        String response = roomServiceImpl.editRoomById(id, roomRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoomById(@PathVariable Long id) {
        String response = roomServiceImpl.deleteRoomById(id);
        if (response.equals("Room deleted successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomResponse>> findAllRoomsByHotelId(@PathVariable Long hotelId) {
        List<RoomResponse> rooms = roomServiceImpl.findAllRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }
    @GetMapping("/")
    public ResponseEntity<List<RoomResponse>> findAllRoomsByHotelId() {
        List<RoomResponse> rooms = roomServiceImpl.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<RoomResponse>> findAllAvailableHotelRooms(@PathVariable Long hotelId) {
        List<RoomResponse> availableRooms = roomServiceImpl.findAllAvailableHotelRooms(hotelId);
        return ResponseEntity.ok(availableRooms);
    }

    @PutMapping("/hotel/{hotelId}/room/{roomId}/deactivate")
    public ResponseEntity<RoomResponse> deactivateRoom(
            @PathVariable Long hotelId,
            @PathVariable Long roomId
    ) {
        RoomResponse response = roomServiceImpl.deactivateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/hotel/{hotelId}/room/{roomId}/activate")
    public ResponseEntity<RoomResponse> activateRoom(
            @PathVariable Long hotelId,
            @PathVariable Long roomId
    ) {
        RoomResponse response = roomServiceImpl.activateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/hotel/{hotelId}/filter")
    public ResponseEntity<List<RoomResponse>> filterRoomsByType(
            @PathVariable Long hotelId,
            @RequestParam String type
    ) {
        List<RoomResponse> response = roomServiceImpl.filterHotelRoomByType(hotelId, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RoomResponse>> filterRoomsByPriceAndState(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam Location location
    ) {
        List<RoomResponse> response = roomServiceImpl.filterByPriceAndLocation(minPrice, maxPrice, location);
        return ResponseEntity.ok(response);
    }

}