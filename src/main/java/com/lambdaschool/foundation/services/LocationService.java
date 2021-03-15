package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.models.Location;

import java.util.List;

public interface LocationService
{
    List<Location> findAll();

    Location findLocationById(long locationid);

    void delete(long locationid);

    Location update(long locationid, String zipcode);

    Location save( long userid, String zipcode);


    void deleteAll();
}
