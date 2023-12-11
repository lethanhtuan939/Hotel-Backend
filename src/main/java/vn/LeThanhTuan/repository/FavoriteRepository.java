package vn.LeThanhTuan.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.LeThanhTuan.entity.Favorite;
import vn.LeThanhTuan.entity.Hotel;
import vn.LeThanhTuan.entity.User;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    @Transactional
    void deleteByUserAndHotel(User user, Hotel hotel);

    List<Favorite> findAllByUser(User user);
}
