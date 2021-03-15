package com.lambdaschool.foundation.repository;

import com.lambdaschool.foundation.models.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, Long>
{
    List<Location> findLocationsByUser_Userid(long userid);
}
