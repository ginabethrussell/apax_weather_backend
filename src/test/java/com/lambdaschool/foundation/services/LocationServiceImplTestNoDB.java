package com.lambdaschool.foundation.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.foundation.FoundationApplicationTesting;
import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.Location;
import com.lambdaschool.foundation.models.Role;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.models.UserRoles;
import com.lambdaschool.foundation.repository.LocationRepository;
import com.lambdaschool.foundation.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoundationApplicationTesting.class,
    properties = {
    "command.line.runner.enabled=false"
    })
public class LocationServiceImplTestNoDB
{

    @Autowired
    private LocationService locationService;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean UserService userService;

    private List<User> userList;
    private List<Location> locationList;

    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();
        locationList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);


        // admin, user
        User u1 = new User("admin",
            "password",
            "admin@clearweather.com");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));

        Location l1 = new Location("40515", u1);
        l1.setLocationid(90);
        locationList.add(l1);

        Location l2 = new Location("96813", u1);
        l2.setLocationid(91);
        locationList.add(l2);

        Location l3 = new Location("33040", u1);
        l3.setLocationid(92);
        locationList.add(l3);

        Location l4 = new Location("04609", u1);
        l4.setLocationid(93);
        locationList.add(l4);

        Location l5 = new Location("99705", u1);
        l5.setLocationid(94);
        locationList.add(l5);

        u1.getLocations()
            .add(l1);
        u1.getLocations()
            .add(l2);
        u1.getLocations()
            .add(l3);
        u1.getLocations().add(l4);
        u1.getLocations().add(l5);

        u1.setUserid(100);

        userList.add(u1);

        // user
        User u2 = new User("user",
            "password",
            "user@gmail.com");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));

        Location l6 = new Location("40515", u2);
        l6.setLocationid(95);
        locationList.add(l6);

        Location l7 = new Location("96813", u2);
        l7.setLocationid(96);
        locationList.add(l7);

        Location l8 = new Location("33040", u2);
        l8.setLocationid(97);
        locationList.add(l8);

        u2.getLocations()
            .add(l6);
        u2.getLocations()
            .add(l7);
        u2.getLocations()
            .add(l8);

        u2.setUserid(200);

        userList.add(u2);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findAll()
    {
        Mockito.when(locationRepository.findAll())
            .thenReturn(locationList);
        assertEquals(8, locationService.findAll().size());
    }

    @Test
    public void findLocationById()
    {
        Mockito.when(locationRepository.findById(any(Long.class)))
            .thenReturn(Optional.of(locationList.get(0)));

        assertEquals("40515", locationService.findLocationById(90).getZipcode());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findLocationByIdNotFound()
    {
        Mockito.when(locationRepository.findById(any(Long.class)))
            .thenReturn(Optional.empty());

        assertEquals("40515", locationService.findLocationById(999).getZipcode());
    }

    @Test
    public void findAllByUserId()
    {
        Mockito.when(locationRepository.findLocationsByUser_Userid(any(Long.class)))
            .thenReturn(locationList);

        assertEquals(8, locationService.findAllByUserId(any(Long.class)).size());
    }

    @Test
    public void delete()
    {
        Mockito.when(locationRepository.findById(any(Long.class)))
            .thenReturn(Optional.of(locationList.get(0)));
        Mockito.doNothing()
            .when(locationRepository).deleteById(any(Long.class));

        locationService.delete(90);
        assertEquals(8, locationList.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteNotFound()
    {
        Mockito.when(locationRepository.findById(any(Long.class)))
            .thenReturn(Optional.empty());
        Mockito.doNothing()
            .when(locationRepository).deleteById(any(Long.class));

        locationService.delete(999);
        assertEquals(8, locationList.size());
    }

    @Test
    public void update() throws Exception
    {
        String zipcode = "12345";
        Location newLocation = new Location();
        newLocation.setUser(userList.get(0));
        newLocation.setZipcode(zipcode);
        newLocation.setLocationid(999);

        ObjectMapper objectMapper = new ObjectMapper();
        Location copyLocation = objectMapper
            .readValue(objectMapper.writeValueAsString(newLocation), Location.class);

        Mockito.when(locationRepository.findById(999L))
            .thenReturn(Optional.of(copyLocation));

        Mockito.when(locationRepository.save(any(Location.class)))
            .thenReturn(newLocation);

        Location addedLocation = locationService.update(999, zipcode);

        assertNotNull(addedLocation);
        assertEquals(newLocation.getZipcode(),
            addedLocation.getZipcode());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateNotFound() throws Exception
    {
        String zipcode = "12345";
        Location newLocation = new Location();
        newLocation.setUser(userList.get(0));
        newLocation.setZipcode(zipcode);
        newLocation.setLocationid(999);

        ObjectMapper objectMapper = new ObjectMapper();
        Location copyLocation = objectMapper
            .readValue(objectMapper.writeValueAsString(newLocation), Location.class);

        Mockito.when(locationRepository.findById(999L))
            .thenReturn(Optional.empty());

        Mockito.when(locationRepository.save(any(Location.class)))
            .thenReturn(newLocation);

        Location addedLocation = locationService.update(999, zipcode);

        assertNotNull(addedLocation);
        assertEquals(newLocation.getZipcode(),
            addedLocation.getZipcode());

    }


    @Test
    public void save() throws Exception
    {
        String zipcode = "12345";
        Location newLocation = new Location();
        newLocation.setUser(userList.get(0));
        newLocation.setZipcode(zipcode);
        newLocation.setLocationid(999);

        ObjectMapper objectMapper = new ObjectMapper();
        Location copyLocation = objectMapper
            .readValue(objectMapper.writeValueAsString(newLocation), Location.class);

        Mockito.when(locationRepository.findById(any(Long.class)))
            .thenReturn(Optional.of(locationList.get(0)));

        Mockito.when(userService.findUserById(any(Long.class)))
            .thenReturn(userList.get(0));

        Mockito.when(locationRepository.save(any(Location.class)))
            .thenReturn(newLocation);

        Location addedLocation = locationService.update(999, zipcode);

        assertNotNull(addedLocation);
        assertEquals(newLocation.getZipcode(),
            addedLocation.getZipcode());
    }


    @Test(expected = ResourceNotFoundException.class)
    public void saveNotFound() throws Exception
    {
        String zipcode = "12345";
        Location newLocation = new Location();
        newLocation.setUser(userList.get(0));
        newLocation.setZipcode(zipcode);
        newLocation.setLocationid(999);

        ObjectMapper objectMapper = new ObjectMapper();
        Location copyLocation = objectMapper
            .readValue(objectMapper.writeValueAsString(newLocation), Location.class);

        Mockito.when(locationRepository.findById(any(Long.class)))
            .thenReturn(Optional.empty());

        Mockito.when(userService.findUserById(any(Long.class)))
            .thenReturn(userList.get(0));

        Mockito.when(locationRepository.save(any(Location.class)))
            .thenReturn(newLocation);

        Location addedLocation = locationService.update(999, zipcode);

        assertNotNull(addedLocation);
        assertEquals(newLocation.getZipcode(),
            addedLocation.getZipcode());
    }

    @Test
    public void deleteAll()
    {
        Mockito.doNothing()
            .when(locationRepository).deleteAll();
        locationService.deleteAll();
        assertEquals(8, locationList.size());
    }
}