package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.FoundationApplicationTesting;
import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.Location;
import com.lambdaschool.foundation.models.Role;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.models.UserRoles;
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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoundationApplicationTesting.class,
    properties = {
    "command.line.runner.enabled=false"
    })
public class UserServiceImplTestNoDB
{

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleService roleService;

    @MockBean
    private HelperFunctions helperFunctions;

    private List<User> userList;

    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();

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

        u1.getLocations()
            .add(new Location( "40515", u1));
        u1.getLocations().get(0).setLocationid(10);

        u1.getLocations()
            .add(new Location( "96813", u1));
        u1.getLocations().get(1).setLocationid(20);

        u1.getLocations()
            .add(new Location( "33040", u1));
        u1.getLocations().get(2).setLocationid(30);

        u1.getLocations()
            .add(new Location( "04609", u1));
        u1.getLocations().get(3).setLocationid(40);

        u1.getLocations()
            .add(new Location( "99705", u1));
        u1.getLocations().get(4).setLocationid(50);

        u1.setUserid(100);

        userList.add(u1);

        // user
        User u2 = new User("user",
            "password",
            "user@gmail.com");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));

        u2.getLocations()
            .add(new Location( "40515", u2));
        u2.getLocations().get(0).setLocationid(11);

        u2.getLocations()
            .add(new Location("31088", u2));
        u2.getLocations().get(1).setLocationid(21);

        u2.getLocations()
            .add(new Location( "37127", u2));
        u2.getLocations().get(2).setLocationid(31);

        u2.setUserid(200);

        userList.add(u2);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findUserById()
    {
        Mockito.when(userRepository.findById(100L))
            .thenReturn(Optional.of(userList.get(0)));

        assertEquals("admin",
            userService.findUserById(100L)
                .getUsername());
    }

    @Test
    public void findByNameContaining()
    {
        Mockito.when(userRepository.findByUsernameContainingIgnoreCase("a"))
            .thenReturn(userList);

        assertEquals(2,
            userService.findByNameContaining("a")
                .size());
    }

    @Test
    public void findAll()
    {
        Mockito.when(userRepository.findAll())
            .thenReturn(userList);

        assertEquals(2,
            userService.findAll()
                .size());
    }

    @Test
    public void delete()
    {
        Mockito.when(userRepository.findById(100L))
            .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing()
            .when(userRepository)
            .deleteById(100L);

        userService.delete(100L);
        assertEquals(2,
            userList.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFoundDelete()
    {
        Mockito.when(userRepository.findById(10L))
            .thenReturn(Optional.empty());

        Mockito.doNothing()
            .when(userRepository)
            .deleteById(10L);

        userService.delete(10L);
        assertEquals(2,
            userList.size());
    }

    @Test
    public void findByName()
    {
        Mockito.when(userRepository.findByUsername("admin"))
            .thenReturn(userList.get(0));

        assertEquals("admin",
            userService.findByName("admin")
                .getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByNameNotFound()
    {
        Mockito.when(userRepository.findByUsername("notUser"))
            .thenReturn(null);

        assertEquals("notUser",
            userService.findByName("notUser")
                .getUsername());
    }

    @Test
    public void save()
    {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u3 = new User("williard",
            "ILuvWeather!",
            "williard@weather.com");
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        u3.getLocations()
            .add(new Location( "12345",
                u3));

        Mockito.when(userRepository.save(any(User.class)))
            .thenReturn(u3);

        Mockito.when(roleService.findRoleById(2))
            .thenReturn(r2);

        assertEquals("williard",
            userService.save(u3)
                .getUsername());
    }

    @Test
    public void update()
    {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u3 = new User("williard",
            "ILuvWeather!",
            "williard@weather.com");
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        u3.getLocations()
            .add(new Location( "12345",
                u3));
        u3.setUserid(300L);

        Mockito.when(roleService.findRoleById(2))
            .thenReturn(r2);

        Mockito.when(userRepository.findById(300L))
            .thenReturn(Optional.of(userList.get(0)));

        Mockito.when(userRepository.save(any(User.class)))
            .thenReturn(u3);

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
            .thenReturn(true);

        assertEquals("12345",
            userService.update(u3,
                300L)
                .getLocations()
                .get(0)
                .getZipcode());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateNotFound()
    {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u3 = new User("williard",
            "ILuvWeather!",
            "williard@weather.com");
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        u3.getLocations()
            .add(new Location( "12345",
                u3));
        u3.setUserid(300L);

        Mockito.when(roleService.findRoleById(2))
            .thenReturn(r2);

        Mockito.when(userRepository.findById(400L))
            .thenReturn(Optional.empty());

        Mockito.when(userRepository.save(any(User.class)))
            .thenReturn(u3);

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
            .thenReturn(false);

        assertEquals("12345",
            userService.update(u3,
                300L)
                .getLocations()
                .get(0)
                .getZipcode());
    }

    @Test
    public void deleteAll()
    {
        Mockito.doNothing()
            .when(userRepository)
            .deleteAll();

        userService.deleteAll();
        assertEquals(2,
            userList.size());
    }
}