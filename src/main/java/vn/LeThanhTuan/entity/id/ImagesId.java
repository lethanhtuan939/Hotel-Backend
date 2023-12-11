package vn.LeThanhTuan.entity.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.LeThanhTuan.entity.Room;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagesId implements Serializable {
    private int id;
    private Room room;
}
