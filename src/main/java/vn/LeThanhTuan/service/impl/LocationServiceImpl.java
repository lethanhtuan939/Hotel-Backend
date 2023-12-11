package vn.LeThanhTuan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.LeThanhTuan.entity.Location;
import vn.LeThanhTuan.exception.ResourceNotFoundException;
import vn.LeThanhTuan.repository.LocationRepository;
import vn.LeThanhTuan.service.LocationService;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location findById(int id) {
        return locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));
    }
}
