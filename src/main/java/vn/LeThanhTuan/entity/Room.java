package vn.LeThanhTuan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import vn.LeThanhTuan.entity.id.RoomId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@IdClass(RoomId.class)
public class Room {

    @Id
    private int id;

    private String name;

    private int floor;

    private double price;

    private String image;

    private String state;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private double sale;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
}
