package vn.LeThanhTuan.service;

import vn.LeThanhTuan.entity.Favorite;
import vn.LeThanhTuan.entity.dto.FavoriteDto;

import java.util.List;

public interface FavoriteService {
    List<FavoriteDto> findListFavorite(int userId);

    FavoriteDto addFavorite(int userId, int hotelId);

    void removeFavorite(int userId, int hotelId);

    void removeFavorite(int id);
}
