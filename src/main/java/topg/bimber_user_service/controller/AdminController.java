package topg.bimber_user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.State;
import topg.bimber_user_service.service.AdminServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateDetailsResponse> updateAdmin(@RequestBody UpdateDetailsRequest updateUserRequest) {
        UpdateDetailsResponse response = adminServiceImpl.updateAdmin(updateUserRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/hotels")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponseDto> createHotel(@RequestBody CreateHotelDto createHotelDto) {
        HotelResponseDto response = adminServiceImpl.createHotel(createHotelDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/hotels/state/{state}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDtoFilter>> getHotelsByState(@PathVariable State state) {
        List<HotelDtoFilter> hotels = adminServiceImpl.getHotelsByState(state);
        return ResponseEntity.ok(hotels);
    }

    @PutMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponseDto> editHotelById(@PathVariable Long id,
                                                          @RequestBody HotelRequestDto updatedHotelDto) {
        HotelResponseDto response = adminServiceImpl.editHotelById(id, updatedHotelDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelDtoFilter> getHotelById(@PathVariable Long id) {
        HotelDtoFilter response = adminServiceImpl.getHotelById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotelById(@PathVariable Long id) {
        String response = adminServiceImpl.deleteHotelById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotels/most-booked/{state}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDtoFilter>> getMostBookedHotelInState(@PathVariable State state) {
        List<HotelDtoFilter> hotels = adminServiceImpl.getMostBookedHotelInState(state);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/hotels/in-state/{state}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDtoFilter>> getHotelsInState(@PathVariable State state) {
        List<HotelDtoFilter> hotels = adminServiceImpl.getHotelsInState(state);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/filter/price-and-state")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomResponse>> filterByPriceAndState(
            @RequestParam("minPrice") BigDecimal minPrice,
            @RequestParam("maxPrice") BigDecimal maxPrice,
            @RequestParam("state") State state) {
        List<RoomResponse> rooms = adminServiceImpl.filterByPriceAndState(minPrice, maxPrice, state);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelId}/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomResponse>> filterHotelRoomByType(
            @PathVariable("hotelId") long hotelId,
            @PathVariable("type") String type) {
        List<RoomResponse> rooms = adminServiceImpl.filterHotelRoomByType(hotelId, type);
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping("/hotel/{hotelId}/deactivate/{roomId}")
    public ResponseEntity<RoomResponse> deactivateRoomByHotelId(
            @PathVariable("hotelId") long hotelId,
            @PathVariable("roomId") long roomId) {
        RoomResponse response = adminServiceImpl.deactivateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/hotel/{hotelId}/activate/{roomId}")
    public ResponseEntity<RoomResponse> activateRoomByHotelId(
            @PathVariable("hotelId") long hotelId,
            @PathVariable("roomId") long roomId) {
        RoomResponse response = adminServiceImpl.activateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotel/{hotelId}/available")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomResponse>> findAllAvailableHotelRooms(
            @PathVariable("hotelId") long hotelId) {
        List<RoomResponse> rooms = adminServiceImpl.findAllAvailableHotelRooms(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> isRoomAvailable(
            @PathVariable("roomId") long roomId) {
        boolean isAvailable = adminServiceImpl.isRoomAvailable(roomId);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/hotel/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomResponse>> findAllRoomsByHotelId(
            @PathVariable("hotelId") long hotelId) {
        List<RoomResponse> rooms = adminServiceImpl.findAllRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoomById(
            @PathVariable("roomId") long roomId) {
        String response = adminServiceImpl.deleteRoomById(roomId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> editRoomById(
            @PathVariable("roomId") long roomId,
            @RequestBody RoomRequest roomRequest) {
        String response = adminServiceImpl.editRoomById(roomId, roomRequest);
        return ResponseEntity.ok(response);
    }


}