package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{

    @Autowired
    private UserService userService;


    @GetMapping(value = "/users",
        produces = "application/json")
    public ResponseEntity<?> listAllUsers()
    {
        List<User> myUsers = userService.findAll();

        System.out.println(SecurityContextHolder.getContext()
            .getAuthentication().getName());

        return new ResponseEntity<>(myUsers,
            HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}",
        produces = "application/json")
    public ResponseEntity<?> getUserById(
        @PathVariable
            Long userId)
    {
        User u = userService.findUserById(userId);
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }

    @GetMapping(value = "/user/name/{userName}",
        produces = "application/json")
    public ResponseEntity<?> getUserByName(
        @PathVariable
            String userName)
    {
        User u = userService.findByName(userName);
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }

    @GetMapping(value = "/user/name/like/{userName}",
        produces = "application/json")
    public ResponseEntity<?> getUserLikeName(
        @PathVariable
            String userName)
    {
        List<User> u = userService.findByNameContaining(userName);
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }

    @PostMapping(value = "/user",
        consumes = "application/json")
    public ResponseEntity<?> addNewUser(
        @Valid
        @RequestBody
            User newuser) throws
                          URISyntaxException
    {
        newuser.setUserid(0);
        newuser = userService.save(newuser);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{userid}")
            .buildAndExpand(newuser.getUserid())
            .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null,
            responseHeaders,
            HttpStatus.CREATED);
    }

    @PutMapping(value = "/user/{userid}",
        consumes = "application/json")
    public ResponseEntity<?> updateFullUser(
        @Valid
        @RequestBody
            User updateUser,
        @PathVariable
            long userid)
    {
        updateUser.setUserid(userid);
        userService.save(updateUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/user/{id}",
        consumes = "application/json")
    public ResponseEntity<?> updateUser(
        @RequestBody
            User updateUser,
        @PathVariable
            long id)
    {
        userService.update(updateUser,
            id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUserById(
        @PathVariable
            long id)
    {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/getuserinfo",
        produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication)
    {
        User u = userService.findByName(authentication.getName());
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }
}