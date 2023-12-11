package vn.LeThanhTuan.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDto {
    private int id;
    private UserDto user;
    private HotelDto hotel;
}
