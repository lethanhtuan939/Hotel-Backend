package vn.LeThanhTuan.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.LeThanhTuan.entity.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {
    private int id;

    private String name;

    private String image;

    private boolean active;

    private double acreage;

    private double rating;

    private Location location;

    public HotelDto(int id, String name, String image, double rating) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rating = rating;
    }
}
