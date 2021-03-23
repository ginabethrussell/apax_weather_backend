package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.ErrorDetail;
import com.lambdaschool.foundation.models.Location;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.LocationService;
import com.lambdaschool.foundation.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @ApiOperation(value = "returns all locations",
        response = Location.class,
        responseContainer = "List")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
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

    @ApiOperation(value = "returns a location from the path parameter locationid",
        response = Location.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "Location Found",
            response = Location.class),
        @ApiResponse(code = 404,
            message = "Location Not Found",
            response = ResourceNotFoundException.class)})
    @GetMapping(value = "/location/{locationId}",
        produces = "application/json")
    public ResponseEntity<?> getLocationById(
        @ApiParam(value = "locationid",
            required = true,
            example = "7")
        @PathVariable
            Long locationId)
    {
        Location l = locationService.findLocationById(locationId);
        return new ResponseEntity<>(l,
            HttpStatus.OK);
    }

    @ApiOperation(value = "creates a location from path parameters",
    response = Void.class)
    @ApiResponses(value = {
    @ApiResponse(code = 201,
        message = "Location Created",
        response = Void.class),
    @ApiResponse(code = 400,
        message = "Bad Request",
        response = ErrorDetail.class)})
    @PostMapping(value = "/location/{userid}/zipcode/{zipcode}")
    public ResponseEntity<?> addNewLocation(
        @ApiParam(value = "userid",
            required = true,
            example = "12")
        @PathVariable
            long userid,
        @ApiParam(value = "zipcode",
            required = true,
            example = "12345")
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

    @ApiOperation(value = "updates a location from data given in the path parameters",
        response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "Location Updated",
            response = Void.class),
        @ApiResponse(code = 404,
            message = "Location not Found",
            response = ResourceNotFoundException.class),
        @ApiResponse(code = 400,
            message = "Bad Request",
            response = ErrorDetail.class)})
    @PutMapping(value = "/location/{locationid}/zipcode/{zipcode}")
    public ResponseEntity<?> updateLocation(
        @ApiParam(value = "locationid",
            required = true,
            example = "12")
        @PathVariable
            long locationid,
        @ApiParam(value = "zipcode",
            required = true,
            example = "12345")
        @PathVariable
            String zipcode)
    {
        locationService.update(locationid, zipcode);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "deletes a location by the locationid",
        response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "Location Found"),
        @ApiResponse(code = 404,
            message = "Location Not Found",
            response = ResourceNotFoundException.class)})
    @DeleteMapping(value = "/location/{locationid}")
    public ResponseEntity<?> deleteLocationById(
        @ApiParam(value = "locationid",
            required = true,
            example = "12")
        @PathVariable
            long locationid)
    {
        locationService.delete(locationid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "returns all locations for the currently authenticated user",
        response = Location.class,
        responseContainer = "List")
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