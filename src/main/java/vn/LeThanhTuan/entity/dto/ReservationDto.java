package vn.LeThanhTuan.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private int id;
    private String bookingDate;
    private String dayStart;
    private String dayEnd;
    private double price;
    private String paymentMethod;
    private String status;
    private String notes;
    private UserDto user;
    private RoomDto room;
}
