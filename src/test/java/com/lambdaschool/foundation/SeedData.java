package com.lambdaschool.foundation;

import com.lambdaschool.foundation.models.Location;
import com.lambdaschool.foundation.models.Role;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.models.UserRoles;
import com.lambdaschool.foundation.services.LocationService;
import com.lambdaschool.foundation.services.RoleService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@ConditionalOnProperty(
    prefix = "command.line.runner",
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Component
public class SeedData
    implements CommandLineRunner
{

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    LocationService locationService;


    @Transactional
    @Override
    public void run(String[] args) throws
                                   Exception
    {
        userService.deleteAll();
        roleService.deleteAll();
        locationService.deleteAll();

        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);

        // admin, user
        User u1 = new User("gina russell",
            "password",
            "ginabeth.russell@gmail.com");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.getLocations()
            .add(new Location( "40515", u1));

        userService.save(u1);

        // user
        User u2 = new User("susannah russell",
            "1234567",
            "susannah@gmail.com");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.getLocations()
            .add(new Location( "40515", u2));
        u2.getLocations()
            .add(new Location("31088", u2));
        u2.getLocations()
            .add(new Location( "37127", u2));

        userService.save(u2);

        // user
        User u3 = new User("matthew russell",
            "ILuvM4th!",
            "mbr4477@gmail.com");
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        u3.getLocations()
            .add(new Location( "40515", u3));
        u3.getLocations()
            .add(new Location( "77058", u3));
        userService.save(u3);

    }
}