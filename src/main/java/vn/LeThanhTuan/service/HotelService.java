package vn.LeThanhTuan.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.LeThanhTuan.entity.Hotel;
import vn.LeThanhTuan.entity.dto.HotelDto;
import vn.LeThanhTuan.entity.dto.RoomDto;

import java.io.IOException;
import java.util.List;

public interface HotelService {

    Page<HotelDto> findAllHotels(String keyword, int currPage, int pageSize);

    HotelDto findById(int id);

    HotelDto createHotel(HotelDto hotelDto, MultipartFile file) throws IOException;

    HotelDto updateHotel(HotelDto hotelDto, MultipartFile file, int id) throws IOException;

    List<HotelDto> findAllList(String location);
}
