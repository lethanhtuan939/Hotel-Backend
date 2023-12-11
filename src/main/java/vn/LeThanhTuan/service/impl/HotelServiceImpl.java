package vn.LeThanhTuan.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.LeThanhTuan.common.FileUtil;
import vn.LeThanhTuan.entity.Hotel;
import vn.LeThanhTuan.entity.Location;
import vn.LeThanhTuan.entity.dto.HotelDto;
import vn.LeThanhTuan.exception.ResourceNotFoundException;
import vn.LeThanhTuan.repository.HotelRepository;
import vn.LeThanhTuan.repository.LocationRepository;
import vn.LeThanhTuan.repository.RoomRepository;
import vn.LeThanhTuan.service.HotelService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    RoomRepository roomRepository;

    public HotelDto toDto(Hotel hotel) {
        return modelMapper.map(hotel, HotelDto.class);
    }

    public Hotel toHotel(HotelDto hotelDto) {
        return modelMapper.map(hotelDto, Hotel.class);
    }

    @Override
    public Page<HotelDto> findAllHotels(String keyword, int currPage, int pageSize) {
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(currPage-1, pageSize, sort);

        Page<Hotel> hotels;

        if(keyword.trim().isEmpty()) {
            hotels = hotelRepository.findAll(pageable);
        } else {
            hotels = hotelRepository.findAllByKeyword(keyword, pageable);
        }
        return hotels.map(this::toDto);
    }

    @Override
    public HotelDto findById(int id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));
        return toDto(hotel);
    }

    @Override
    public HotelDto createHotel(HotelDto hotelDto, MultipartFile file) throws IOException {
        Hotel hotel = toHotel(hotelDto);
        Location location = locationRepository.findById(hotelDto.getLocation().getId()).orElseThrow(() -> new ResourceNotFoundException("Location", "id", hotelDto.getLocation().getId()));

        String fileName = FileUtil.generatedFileName(file);

        hotel.setLocation(location);
        hotel.setImage(fileName);
        hotel.setActive(hotelDto.isActive());

        FileUtil.saveFile(fileName, file);

        Hotel savedHotel = hotelRepository.save(hotel);

        return toDto(savedHotel);
    }

    @Override
    public HotelDto updateHotel(HotelDto hotelDto, MultipartFile file, int id) throws IOException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow((() -> new ResourceNotFoundException("Hotel", "id", id)));
        Location location = locationRepository.findById(hotelDto.getLocation().getId()).orElseThrow(() -> new ResourceNotFoundException("Location", "id", hotelDto.getLocation().getId()));

        if(file != null) {
            String fileName = FileUtil.generatedFileName(file);
            hotel.setImage(fileName);
            FileUtil.saveFile(fileName, file);
        }

        hotel.setAcreage(hotelDto.getAcreage());
        hotel.setName(hotelDto.getName());
        hotel.setLocation(location);
        hotel.setRating(hotelDto.getRating());
        hotel.setActive(hotelDto.isActive());

        Hotel updatedHotel = hotelRepository.save(hotel);

        return toDto(updatedHotel);
    }

    @Override
    public List<HotelDto> findAllList(String location) {
        List<Hotel> hotels = hotelRepository.findAllByLocation(location);

        return hotels.stream().map(this::toDto).collect(Collectors.toList());
    }
}
