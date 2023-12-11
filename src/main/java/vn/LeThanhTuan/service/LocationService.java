package vn.LeThanhTuan.service;

import vn.LeThanhTuan.entity.Location;

import java.util.List;

public interface LocationService {
    List<Location> findAll();

    Location findById(int id);
}
