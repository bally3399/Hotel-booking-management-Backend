package topg.bimber_user_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import topg.bimber_user_service.dto.requests.CreateHotelDto;
import topg.bimber_user_service.dto.requests.HotelDtoFilter;
import topg.bimber_user_service.dto.requests.HotelRequestDto;
import topg.bimber_user_service.dto.responses.BaseResponse;
import topg.bimber_user_service.dto.responses.HotelResponseDto;
import topg.bimber_user_service.exceptions.*;
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

    @GetMapping("/state/{state}")
    public ResponseEntity<?> getHotelsByState(@PathVariable String state) {
            List<HotelDtoFilter> hotels = hotelServiceImpl.getHotelsByState(state);
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


    @GetMapping("/count")
    public ResponseEntity<?> getTotalHotelsInState(@RequestParam String state) {
            var response = hotelServiceImpl.getTotalHotelsInState(state);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,response));
    }


    @GetMapping("/most-booked/{state}")
    public ResponseEntity<?> getMostBookedHotelsByState(@PathVariable String state) {
            List<HotelDtoFilter> hotels = hotelServiceImpl.getMostBookedHotelsByState(state);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,hotels));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {
            String message = hotelServiceImpl.deleteHotelById(id);
            return ResponseEntity.status(200).body(new BaseResponse<>(true,message));
    }
    
}


