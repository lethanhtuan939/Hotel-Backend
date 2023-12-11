package vn.LeThanhTuan.entity.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.LeThanhTuan.entity.Hotel;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomId implements Serializable {
    private int id;
    private Hotel hotel;
}
