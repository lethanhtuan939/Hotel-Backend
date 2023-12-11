package vn.LeThanhTuan.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.LeThanhTuan.entity.Location;
import vn.LeThanhTuan.entity.Role;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    private int id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private boolean enabled;
    private String gender;
    private String image;
    private String phoneNumber;
    private Location location;
    private Set<Role> roles;

    public UserDto(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
