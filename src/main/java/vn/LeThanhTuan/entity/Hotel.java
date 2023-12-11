package vn.LeThanhTuan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Hotel {

    @Id
    private int id;
    private String name;
    private String image;
    private boolean active;
    private double acreage;
    private double rating;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Favorite> favorites = new ArrayList<>();
}
