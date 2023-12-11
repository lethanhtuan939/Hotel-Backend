package vn.LeThanhTuan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.LeThanhTuan.entity.Reservation;
import vn.LeThanhTuan.entity.User;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByUserAndStatus(User user, String status);

    @Query("SELECT r FROM Reservation r WHERE r.user = :user AND (r.status LIKE %:pending% OR r.status LIKE %:accepted%)")
    List<Reservation> findAllBStatusPendingAndAccepted(User user, String pending, String accepted);

    @Query("SELECT MONTH(r.dayEnd) AS month, SUM(r.price) AS totalRevenue " +
            "FROM Reservation r WHERE YEAR(r.dayEnd) = :year " +
            "GROUP BY MONTH(r.dayEnd) ORDER BY MONTH(r.dayEnd)")
    List<Object[]> findRevenueByMonth(int year);

    @Query("SELECT r FROM Reservation r WHERE r.status LIKE %?1%")
    Page<Reservation> findAllByKeyword(String keyword, Pageable pageable);
}
