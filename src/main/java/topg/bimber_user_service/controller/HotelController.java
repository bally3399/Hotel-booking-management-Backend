package topg.bimber_user_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import topg.bimber_user_service.dto.requests.CreateHotelDto;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.BaseResponse;
import topg.bimber_user_service.dto.responses.HotelResponseDto;
import topg.bimber_user_service.models.Location;
import topg.bimber_user_service.service.HotelServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HotelController {
    private final HotelServiceImpl hotelServiceImpl;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createHotel(@RequestBody CreateHotelDto dto) {
        HotelResponseDto response = hotelServiceImpl.createHotel(dto);
        return ResponseEntity.status(201).body(new BaseResponse<>(true,response));
    }



    @GetMapping("/location/{location}")
    public ResponseEntity<?> getHotelsByLocation(@PathVariable("location") Location location) {
            List<HotelDtoFilter> hotels = hotelServiceImpl.getHotelsByLocation(location);
        return ResponseEntity.status(200).body(new BaseResponse<>(true,hotels));
    }


    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editHotelById(@PathVariable("id") Long id, @RequestBody HotelRequestDto hotelRequestDto) {
            var responseDto = hotelServiceImpl.editHotelById(id, hotelRequestDto);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,responseDto));
    }


    @GetMapping("/hotels/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable("id") Long id) {
            HotelDtoFilter hotel = hotelServiceImpl.getHotelById(id);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,hotel));
    }
    @GetMapping("/{name}")
    public ResponseEntity<?> getHotelByName(@PathVariable("name") String name) {
            HotelDtoFilter hotel = hotelServiceImpl.findByName(name);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,hotel));
    }

 @GetMapping("/hotels/")
    public ResponseEntity<?> getHotels() {
            var hotels = hotelServiceImpl.getAllHotels();
            return ResponseEntity.status(200).body(new BaseResponse<>(true,hotels));
    }


    @GetMapping("/count")
    public ResponseEntity<?> getTotalHotelsInLocation(@RequestParam Location location) {
            var response = hotelServiceImpl.getTotalHotelsByLocation(location);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,response));
    }


    @GetMapping("/most-booked/{location}")
    public ResponseEntity<?> getMostBookedHotelsByLocation(@PathVariable Location location) {
            List<HotelDtoFilter> hotels = hotelServiceImpl.getMostBookedHotelsByLocation(location);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,hotels));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteHotel(@PathVariable("id") Long id) {
            String message = hotelServiceImpl.deleteHotelById(id);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,message));
    }
    
}


