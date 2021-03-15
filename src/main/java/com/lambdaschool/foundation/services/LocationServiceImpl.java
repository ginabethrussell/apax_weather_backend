package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.Location;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.repository.LocationRepository;
import com.lambdaschool.foundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "locationService")
public class LocationServiceImpl
    implements LocationService
{

    @Autowired
    private LocationRepository locationrepos;

    @Autowired
    private UserService userService;


    @Override
    public List<Location> findAll()
    {
        List<Location> list = new ArrayList<>();

        locationrepos.findAll()
        .iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Location findLocationById(long locationid)
    {
        return locationrepos.findById(locationid)
            .orElseThrow(() -> new ResourceNotFoundException("Location id " + locationid + "Not Found"));
    }

    @Override
    public void delete(long locationid)
    {
        if (locationrepos.findById(locationid)
            .isPresent())
        {

            locationrepos.deleteById(locationid);
        } else
        {
            throw new ResourceNotFoundException("Location id " + locationid + " Not Found");
        }

    }

    @Transactional
    @Override
    public Location update(
        long locationid,
        String zipcode)
    {
        Location location = locationrepos.findById(locationid)
            .orElseThrow(() -> new ResourceNotFoundException("Location with id " + locationid + " Not Found"));
        location.setZipcode(zipcode);

        return locationrepos.save(location);

    }

    @Transactional
    @Override
    public Location save(
        long userid,
        String zipcode)
    {
        User currentUser = userService.findUserById(userid);
        Location newLocation = new Location(zipcode, currentUser);
        return locationrepos.save(newLocation);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteAll()
    {
        locationrepos.deleteAll();
    }
}
