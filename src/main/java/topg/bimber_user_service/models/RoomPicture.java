package topg.bimber_user_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    public RoomPicture(String fileName, String fileType, Room room, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.room = room;
        this.data = data;
    }


    public String getUrl() {
        return "/api/room-pictures/" + this.id;
    }
}



