package vn.LeThanhTuan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Location {
    @Id
    private int id;
    private String name;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Hotel> hotels;
}
