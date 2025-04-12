//package topg.bimber_user_service.models;
//
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Builder
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Hotel {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String name;
//    @Enumerated(EnumType.STRING)
//    private Location location;
//
//    @ElementCollection
//    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
//    @Column(name = "amenity")
//    private List<String> amenities;
//
//    @Column(nullable = false, length = 1000)
//    private String description;
//
//    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Room> rooms;
//
//    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments;
//    @ElementCollection
//    @CollectionTable(name = "hotels", joinColumns = @JoinColumn(name = "hotel_id"))
//    @Column(name = "hotel_picture")
//    private List<String> pictures;
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using= LocalDateTimeSerializer.class)
//    private LocalDateTime createdAt;
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using= LocalDateTimeSerializer.class)
//    private LocalDateTime updatedAt;
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//}
package topg.bimber_user_service.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Location location;

    @ElementCollection
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> amenities = new ArrayList<>();

    @Column(nullable = false, length = 1000)
    private String description;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "hotel", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "hotel_picture")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> pictures = new ArrayList<>();

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}