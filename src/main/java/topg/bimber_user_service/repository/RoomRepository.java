package topg.bimber_user_service.repository;

import io.lettuce.core.ScanIterator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import topg.bimber_user_service.dto.responses.NewRoomResponse;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.Location;
import topg.bimber_user_service.models.Room;
import topg.bimber_user_service.models.RoomType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    List<Room> findByHotelIdAndAvailable(Long hotelId, boolean available);
    List<Room> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Room> findByHotelIdAndRoomType(Long hotelId, RoomType roomType);


    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.hotel = :hotel AND r.available = true " +
            "AND NOT EXISTS (SELECT b FROM Booking b WHERE b.room = r AND b.status = 'ACTIVE' " +
            "AND (b.startDate <= :endDate AND b.endDate >= :startDate))")
    List<Room> findAvailableRoomsByRoomTypeAndHotel(RoomType roomType, Hotel hotel, LocalDateTime startDate, LocalDateTime endDate);

    List<Room> findByAvailableTrue();
}
