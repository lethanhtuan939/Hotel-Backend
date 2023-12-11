package vn.LeThanhTuan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.LeThanhTuan.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
