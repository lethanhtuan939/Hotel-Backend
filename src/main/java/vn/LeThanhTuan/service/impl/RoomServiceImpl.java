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
import vn.LeThanhTuan.entity.Room;
import vn.LeThanhTuan.entity.RoomType;
import vn.LeThanhTuan.entity.dto.RoomDto;
import vn.LeThanhTuan.entity.id.RoomId;
import vn.LeThanhTuan.exception.ResourceNotFoundException;
import vn.LeThanhTuan.repository.HotelRepository;
import vn.LeThanhTuan.repository.RoomRepository;
import vn.LeThanhTuan.repository.RoomTypeRepository;
import vn.LeThanhTuan.service.RoomService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RoomTypeRepository roomTypeRepository;

    public Room toRoom(RoomDto roomDto) {
        return modelMapper.map(roomDto, Room.class);
    }

    public RoomDto toDto(Room room) {
        return modelMapper.map(room, RoomDto.class);
    }

    public RoomId createRoomId(int rid, int hid) {
        Hotel hotel = hotelRepository.findById(hid).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hid));
        return RoomId.builder()
                    .id(rid)
                    .hotel(hotel)
                    .build();
    }

    @Override
    public Page<RoomDto> findAllRooms(String keyword, int currPage, int pageSize) {
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(currPage-1, pageSize, sort);

        Page<Room> rooms;
        if(keyword.trim().isEmpty()) {
            rooms = roomRepository.findAll(pageable);
        } else {
            rooms = roomRepository.findAllByKeyword(keyword, pageable);
        }

        return rooms.map(this::toDto);
    }

    @Override
    public RoomDto findById(int rid, int hid) {
        RoomId id = createRoomId(rid, hid);
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room", "id", rid));
        return toDto(room);
    }

    @Override
    public RoomDto updateRoom(int rid, int hid, RoomDto roomDto, MultipartFile file) throws IOException {
        RoomId id = createRoomId(rid, hid);
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room", "id", rid));
        RoomType type = roomTypeRepository.findById(roomDto.getRoomType().getId()).orElseThrow(() -> new ResourceNotFoundException("Room Type", "id", roomDto.getRoomType().getId()));

        if(file != null) {
            String fileName = FileUtil.generatedFileName(file);
            room.setImage(fileName);
            FileUtil.saveFile(fileName, file);
        }

        Hotel hotel = hotelRepository.findById(roomDto.getHotel().getId()).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hid));

        room.setFloor(roomDto.getFloor());
        room.setHotel(hotel);
        room.setName(roomDto.getName());
        room.setRoomType(type);
        room.setSale(roomDto.getSale());
        room.setState(roomDto.getState());
        room.setPrice(roomDto.getPrice());
        room.setFloor(roomDto.getFloor());

        Room updatedRoom = roomRepository.save(room);

        return toDto(updatedRoom);
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto, MultipartFile file) throws IOException {
        Room room = toRoom(roomDto);
        Hotel hotel = hotelRepository.findById(roomDto.getHotel().getId()).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", roomDto.getHotel().getId()));
        RoomType type = roomTypeRepository.findById(roomDto.getRoomType().getId()).orElseThrow(() -> new ResourceNotFoundException("Room Type", "id", roomDto.getRoomType().getId()));

        String fileName = FileUtil.generatedFileName(file);

        room.setHotel(hotel);
        room.setImage(fileName);
        room.setRoomType(type);

        FileUtil.saveFile(fileName, file);

        Room savedRoom = roomRepository.save(room);

        return toDto(savedRoom);
    }

    @Override
    public List<RoomType> findRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Override
    public Page<RoomDto> findRoomByHotel(int hotelId, int currentPage, int pageSize) {
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(currentPage-1, pageSize, sort);

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId));

        Page<Room> rooms = roomRepository.findByHotel(hotel, pageable);

        return rooms.map(this::toDto);
    }

    @Override
    public List<RoomDto> filterByDate(Date dayStart, Date dayEnd) {
        List<Room> rooms = roomRepository.findAvailableRooms(dayStart, dayEnd);

        return rooms.stream().map(this::toDto).collect(Collectors.toList());
    }
}
