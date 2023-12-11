package vn.LeThanhTuan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.LeThanhTuan.entity.Hotel;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @Query("SELECT h FROM Hotel h WHERE h.name LIKE %?1% OR h.acreage LIKE %?1% OR h.location.name LIKE %?1%")
    Page<Hotel> findAllByKeyword(String keyword, Pageable pageable);

    @Query("SELECT h FROM Hotel h WHERE h.location.name LIKE %?1%")
    List<Hotel> findAllByLocation(String keyword);
}
