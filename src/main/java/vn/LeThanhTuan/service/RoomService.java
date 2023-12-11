package vn.LeThanhTuan.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.LeThanhTuan.entity.Room;
import vn.LeThanhTuan.entity.RoomType;
import vn.LeThanhTuan.entity.dto.RoomDto;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface RoomService {
    Page<RoomDto> findAllRooms(String keyword, int currPage, int pageSize);

    RoomDto findById(int rid, int hid);

    RoomDto updateRoom(int rid, int hid, RoomDto roomDto, MultipartFile file) throws IOException;

    RoomDto createRoom(RoomDto roomDto, MultipartFile file) throws IOException;

    List<RoomType> findRoomTypes();

    Page<RoomDto> findRoomByHotel(int hotelId, int currentPage, int pageSize);

    List<RoomDto> filterByDate(Date dayStart, Date dayEnd);
}
