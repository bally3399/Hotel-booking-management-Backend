package topg.bimber_user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import topg.bimber_user_service.models.Hotel;
import topg.bimber_user_service.models.Location;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) = LOWER(:name)")
    Optional<Hotel> findByName(@Param("name") String name);

    List<Hotel> findByLocation(Location location);

    Integer countByLocation(Location location);

    @Query("SELECT h FROM Hotel h JOIN Booking b ON h.id = b.hotel.id " +
            "WHERE h.location = :location " +
            "GROUP BY h.id " +
            "ORDER BY COUNT(b.id) DESC")
    List<Hotel> findMostBookedHotelsByLocation(Location location);

    void deleteByName(String name);

}
