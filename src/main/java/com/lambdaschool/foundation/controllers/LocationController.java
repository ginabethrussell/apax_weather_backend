package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.services.LocationService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
public class LocationController
{
    @Autowired
    private LocationService locationService;
}
