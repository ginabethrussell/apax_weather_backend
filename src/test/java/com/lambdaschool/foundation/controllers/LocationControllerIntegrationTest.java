package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.FoundationApplicationTesting;
import com.lambdaschool.foundation.models.Location;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.LocationService;
import com.lambdaschool.foundation.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import static org.hamcrest.Matchers.containsString;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = FoundationApplicationTesting.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "admin")
public class LocationControllerIntegrationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;

    @Before
    public void setUp() throws Exception
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        List<Location> myLocations = locationService.findAll();
        for(Location l : myLocations)
        {
            System.out.println(l.getLocationid() + " " + l.getZipcode());
        }

        List<User> myUsers = userService.findAll();
        for(User u : myUsers)
        {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listAllLocations()
    {
        given().when()
            .get("/locations/locations")
            .then()
            .statusCode(200)
            .and()
            .body(containsString("40515"));
    }

    @Test
    public void getLocationById()
    {
        long aLocationId = 10;

        given().when()
            .get("/locations/location/" + aLocationId)
            .then()
            .statusCode(200)
            .and()
            .body(containsString("40515"));
    }

    @Test
    public void getLocationByIdNotFound()
    {
        long aLocationId = 99;

        given().when()
            .get("/locations/location/" + aLocationId)
            .then()
            .statusCode(404)
            .and()
            .body(containsString("Resource"));
    }

    @Test
    public void addNewLocation()
    {
        String zipcode = "12345";
        long userid = 9;

        given().when()
            .post("/locations/location/{userid}/zipcode/{zipcode}", userid, zipcode)
            .then()
            .statusCode(201);
    }

    @Test
    public void updateLocation()
    {
        long aLocationId = 6;
        String zipcode = "33041";
        given().when()
            .put("/locations/location/{locationid}/zipcode/{zipcode}", aLocationId, zipcode)
            .then()
            .statusCode(200);
    }

    @Test
    public void deleteLocationById()
    {
        long aLocationId = 11;
        given().when()
            .delete("/locations/location/" + aLocationId)
            .then()
            .statusCode(200);
    }

    @Test
    public void deleteLocationByIdNotFound()
    {
        long notALocationId = 7777;
        given().when()
            .delete("/locations/location/" + notALocationId)
            .then()
            .statusCode(404)
            .and()
            .body(containsString("Resource"));;
    }

    @Test
    public void getCurrentUserLocations() throws Exception
    {
        this.mockMvc.perform(get("/locations/getuserlocations"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("40515")));
    }
}