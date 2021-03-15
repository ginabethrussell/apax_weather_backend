package com.lambdaschool.foundation.repository;

import com.lambdaschool.foundation.models.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long>
{
}
