package vn.LeThanhTuan.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.LeThanhTuan.entity.RoomType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    private int id;

    private String name;

    private int floor;

    private String image;

    private double price;

    private String state;

    private HotelDto hotel;

    private double sale;

    private RoomType roomType;
}
