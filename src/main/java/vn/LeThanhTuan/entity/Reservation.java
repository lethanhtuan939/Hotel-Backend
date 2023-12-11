package vn.LeThanhTuan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.LeThanhTuan.common.CustomDateDeserializer;

import java.util.Date;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
//    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date bookingDate;

        @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
//    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date dayStart;

        @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
//    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date dayEnd;

    private double price;

    private String status;

    private String paymentMethod;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "room_id", referencedColumnName = "id"),
            @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id")
    })
    private Room room;
}
