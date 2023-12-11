package vn.LeThanhTuan.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.LeThanhTuan.entity.Favorite;
import vn.LeThanhTuan.entity.Hotel;
import vn.LeThanhTuan.entity.User;
import vn.LeThanhTuan.entity.dto.FavoriteDto;
import vn.LeThanhTuan.entity.dto.HotelDto;
import vn.LeThanhTuan.entity.dto.UserDto;
import vn.LeThanhTuan.exception.ResourceNotFoundException;
import vn.LeThanhTuan.repository.FavoriteRepository;
import vn.LeThanhTuan.repository.HotelRepository;
import vn.LeThanhTuan.repository.UserRepository;
import vn.LeThanhTuan.service.FavoriteService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    ModelMapper modelMapper;

    public FavoriteDto toDto(Favorite favorite) {
        return modelMapper.map(favorite, FavoriteDto.class);
    }


    @Override
    public List<FavoriteDto> findListFavorite(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Favorite> favorites = favoriteRepository.findAllByUser(user);
        List<FavoriteDto> favoriteDtos = new ArrayList<>();

        for (Favorite favorite : favorites) {
            UserDto userDto = new UserDto(favorite.getUser().getId(), favorite.getUser().getEmail(), favorite.getUser().getName());
            HotelDto hotelDto = new HotelDto(favorite.getHotel().getId(), favorite.getHotel().getName(), favorite.getHotel().getImage(), favorite.getHotel().getRating());

            FavoriteDto dto = new FavoriteDto(favorite.getId(), userDto, hotelDto);
            favoriteDtos.add(dto);
        }

        return favoriteDtos;
    }

    @Override
    public FavoriteDto addFavorite(int userId, int hotelId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setHotel(hotel);

        Favorite favoriteSaved = favoriteRepository.save(favorite);
        FavoriteDto dto = toDto(favoriteSaved);

        dto.setUser(new UserDto(userId, user.getEmail(), user.getName()));
        dto.setHotel(new HotelDto(hotelId, hotel.getName(), hotel.getImage(), hotel.getRating()));

        return dto;
    }

    @Override
    public void removeFavorite(int userId, int hotelId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId));

        favoriteRepository.deleteByUserAndHotel(user, hotel);
    }

    @Override
    public void removeFavorite(int id) {
        favoriteRepository.deleteById(id);
    }
}
