package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.ErrorDetail;
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

    @ApiOperation(value = "returns all users",
        response = User.class,
        responseContainer = "List")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
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

    @ApiOperation(value = "returns a user from the path parameter userid",
        response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "User Found",
            response = User.class),
        @ApiResponse(code = 404,
            message = "User Not Found",
            response = ResourceNotFoundException.class)})
    @GetMapping(value = "/user/{userId}",
        produces = "application/json")
    public ResponseEntity<?> getUserById(
        @ApiParam(value = "userid",
            required = true,
            example = "7")
        @PathVariable
            Long userId)
    {
        User u = userService.findUserById(userId);
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }

    @ApiOperation(value = "returns a user from the path parameter userName",
        response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "User Found",
            response = User.class),
        @ApiResponse(code = 404,
            message = "User Not Found",
            response = ResourceNotFoundException.class)})
    @GetMapping(value = "/user/name/{userName}",
        produces = "application/json")
    public ResponseEntity<?> getUserByName(
        @ApiParam(value = "userName",
            required = true,
            example = "williard")
        @PathVariable
            String userName)
    {
        User u = userService.findByName(userName);
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }

    @ApiOperation(value = "returns a list of users with names like the path parameter userName",
        response = User.class,
        responseContainer = "List")
    @GetMapping(value = "/user/name/like/{userName}",
        produces = "application/json")
    public ResponseEntity<?> getUserLikeName(
        @ApiParam(value = "userName",
            required = true,
            example = "williard")
        @PathVariable
            String userName)
    {
        List<User> u = userService.findByNameContaining(userName);
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }

    @ApiOperation(value = "creates a user from data given in the request body",
        response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201,
            message = "User Created",
            response = Void.class),
        @ApiResponse(code = 400,
            message = "Bad Request",
            response = ErrorDetail.class)})
    @PostMapping(value = "/user",
        consumes = "application/json")
    public ResponseEntity<?> addNewUser(
        @Valid
        @ApiParam(value = "a complete user object",
            required = true)
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

    @ApiOperation(value = "updates a user from data given in the request body and the path parameter userid",
        response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "User Updated",
            response = Void.class),
        @ApiResponse(code = 404,
            message = "User not Found",
            response = ResourceNotFoundException.class),
        @ApiResponse(code = 400,
            message = "Bad Request",
            response = ErrorDetail.class)})
    @PutMapping(value = "/user/{userid}",
        consumes = "application/json")
    public ResponseEntity<?> updateFullUser(
        @ApiParam(value = "a complete user object",
            required = true)
        @Valid
        @RequestBody
            User updateUser,
        @ApiParam(value = "userid",
            required = true,
            example = "7")
        @PathVariable
            long userid)
    {
        updateUser.setUserid(userid);
        userService.save(updateUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "updates a user from data given in the request body and the path parameter userid",
        response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "User Updated",
            response = Void.class),
        @ApiResponse(code = 404,
            message = "User not Found",
            response = ResourceNotFoundException.class),
        @ApiResponse(code = 400,
            message = "Bad Request",
            response = ErrorDetail.class)})
    @PatchMapping(value = "/user/{id}",
        consumes = "application/json")
    public ResponseEntity<?> updateUser(
        @ApiParam(value = "a partial user object",
            required = true)
        @RequestBody
            User updateUser,
        @ApiParam(value = "userid",
            required = true,
            example = "7")
        @PathVariable
            long id)
    {
        userService.update(updateUser,
            id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "deletes a user by the userid",
        response = Void.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200,
            message = "User Found"),
        @ApiResponse(code = 404,
            message = "User Not Found",
            response = ResourceNotFoundException.class)})
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUserById(
        @PathVariable
            long id)
    {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "returns the user data for the currently authenticated user",
        response = User.class)
    @GetMapping(value = "/getuserinfo",
        produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication)
    {
        User u = userService.findByName(authentication.getName());
        return new ResponseEntity<>(u,
            HttpStatus.OK);
    }
}