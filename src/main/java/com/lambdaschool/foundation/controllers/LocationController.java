package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.models.Location;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.LocationService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController
{
    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/locations",
        produces = "application/json")
    public ResponseEntity<?> listAllLocations()
    {
        List<Location> mylocations = locationService.findAll();

        System.out.println(SecurityContextHolder.getContext()
            .getAuthentication().getName());

        return new ResponseEntity<>(mylocations,
            HttpStatus.OK);
    }

    @GetMapping(value = "/location/{locationId}",
        produces = "application/json")
    public ResponseEntity<?> getLocationById(
        @PathVariable
            Long locationId)
    {
        Location l = locationService.findLocationById(locationId);
        return new ResponseEntity<>(l,
            HttpStatus.OK);
    }

    @PostMapping(value = "/location/{userid}/zipcode/{zipcode}")
    public ResponseEntity<?> addNewLocation(
        @PathVariable
            long userid,
        @PathVariable
            String zipcode) throws
                          URISyntaxException
    {

        Location newlocation = locationService.save(userid, zipcode);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newLocationURI = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{locationid}")
            .buildAndExpand(newlocation.getLocationid())
            .toUri();
        responseHeaders.setLocation(newLocationURI);

        return new ResponseEntity<>(null,
            responseHeaders,
            HttpStatus.CREATED);
    }

    @PutMapping(value = "/location/{locationid}/zipcode/{zipcode}")
    public ResponseEntity<?> updateLocation(
        @PathVariable
            long locationid,
        @PathVariable
            String zipcode)
    {
        locationService.update(locationid, zipcode);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping(value = "/location/{locationid}")
    public ResponseEntity<?> deleteLocationById(
        @PathVariable
            long locationid)
    {
        locationService.delete(locationid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/getuserlocations",
        produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserLocations(Authentication authentication)
    {
        User u = userService.findByName(authentication.getName());
        List<Location> userLocations = locationService.findAllByUserId(u.getUserid());
        return new ResponseEntity<>(userLocations,
            HttpStatus.OK);
    }
}