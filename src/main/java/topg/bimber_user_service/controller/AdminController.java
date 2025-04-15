package topg.bimber_user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.*;
import topg.bimber_user_service.dto.responses.*;
import topg.bimber_user_service.models.Location;
import topg.bimber_user_service.service.AdminService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminServiceImpl;

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAdmin(@RequestBody UpdateDetailsRequest updateUserRequest) {
        UpdateDetailsResponse response = adminServiceImpl.updateAdmin(updateUserRequest);
        return ResponseEntity.status(201).body(new BaseResponse<>(true, response));
    }
    @PostMapping("/register") 
    public ResponseEntity<?> register(@RequestBody  UserRequestDto  dto){
        var response = adminServiceImpl.createAdmin(dto);
        return ResponseEntity.status(201).body(new BaseResponse<>(true, response));
    }


    @PostMapping("/hotels")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createHotel(@RequestBody CreateHotelDto createHotelDto) {
        HotelResponseDto response = adminServiceImpl.createHotel(createHotelDto);
        return ResponseEntity.status(201).body(new BaseResponse<>(true, response));
    }
    @PostMapping("/add_room")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addRoom(@RequestBody RoomRequest roomRequest) {
        RoomResponse response = adminServiceImpl.addRoom(roomRequest);
        return ResponseEntity.status(201).body(new BaseResponse<>(true, response));
    }


    @GetMapping("/hotels/state/{location}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getHotelsByLocation(@PathVariable Location location) {
        List<HotelDtoFilter> hotels = adminServiceImpl.getHotelsByLocation(location);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, hotels));

    }

    @PutMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editHotelById(@PathVariable Long id,
                                                          @RequestBody HotelRequestDto updatedHotelDto) {
        HotelResponseDto response = adminServiceImpl.editHotelById(id, updatedHotelDto);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }

    @GetMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getHotelById(@PathVariable Long id) {
        HotelDtoFilter response = adminServiceImpl.getHotelById(id);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }
    @GetMapping("/hotels/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllHotels() {
        List<HotelDtoFilter> response = adminServiceImpl.getAllHotels();
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }

    @GetMapping("/hotels/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getHotelByName(@RequestBody String name) {
        HotelDtoFilter response = adminServiceImpl.findByName(name);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }

    @DeleteMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteHotelById(@PathVariable Long id) {
        String response = adminServiceImpl.deleteHotelById(id);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));

    }

    @GetMapping("/hotels/most-booked/{location}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDtoFilter>> getMostBookedHotelByLocation(@PathVariable Location location) {
        List<HotelDtoFilter> hotels = adminServiceImpl.getMostBookedHotelByLocation(location);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/rooms/hotelId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAllRoomsByHotelId(@RequestBody Long hotelId) {
        List<NewRoomResponse> response = adminServiceImpl.findAllRoomsByHotelId(hotelId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));

    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRooms() {
        List<RoomResponse> response = adminServiceImpl.getAllRooms();
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));

    }



    @GetMapping("/filter/price-and-state")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> filterByPriceAndState(
            @RequestParam("minPrice") BigDecimal minPrice,
            @RequestParam("maxPrice") BigDecimal maxPrice,
            @RequestParam("location") Location location) {
        List<RoomResponse> rooms = adminServiceImpl.filterByPriceAndLocation(minPrice, maxPrice, location);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,rooms));

    }

    @GetMapping("/hotel/{hotelId}/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> filterHotelRoomByType(
            @PathVariable("hotelId") long hotelId,
            @PathVariable("type") String type) {
        List<RoomResponse> rooms = adminServiceImpl.filterHotelRoomByType(hotelId, type);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, rooms));

    }

    @PatchMapping("/hotel/{hotelId}/deactivate/{roomId}")
    public ResponseEntity<?> deactivateRoomByHotelId(
            @PathVariable("hotelId") long hotelId,
            @PathVariable("roomId") long roomId) {
        RoomResponse response = adminServiceImpl.deactivateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }

    @PatchMapping("/hotel/{hotelId}/activate/{roomId}")
    public ResponseEntity<?> activateRoomByHotelId(
            @PathVariable("hotelId") long hotelId,
            @PathVariable("roomId") long roomId) {
        RoomResponse response = adminServiceImpl.activateRoomByHotelId(hotelId, roomId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }

    @GetMapping("/hotel/{hotelId}/available")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAllAvailableHotelRooms(
            @PathVariable("hotelId") long hotelId) {
        List<RoomResponse> rooms = adminServiceImpl.findAllAvailableHotelRooms(hotelId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, rooms));
    }

    @GetMapping("/{roomId}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> isRoomAvailable(
            @PathVariable("roomId") long roomId) {
        boolean isAvailable = adminServiceImpl.isRoomAvailable(roomId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, isAvailable));
    }

    @GetMapping("/hotel/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAllRoomsByHotelId(
            @PathVariable("hotelId") long hotelId) {
        List<RoomResponse> rooms = adminServiceImpl.findAllRoomsByHotelId(hotelId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, rooms));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoomById(
            @PathVariable("roomId") long roomId) {
        DeleteRoomResponse response = adminServiceImpl.deleteRoomById(roomId);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editRoomById(
            @PathVariable("roomId") long roomId,
            @RequestBody RoomRequest roomRequest) {
        EditRoomResponse response = adminServiceImpl.editRoomById(roomId, roomRequest);
        return ResponseEntity.status(200).body(new BaseResponse<>(true, response));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getTotalHotelsByLocation(@RequestParam Location location) {
        var response = adminServiceImpl.getTotalHotelsByLocation(location);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,response));
    }


}