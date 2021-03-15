package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.models.UserMinimum;
import com.lambdaschool.foundation.models.UserRoles;
import com.lambdaschool.foundation.services.RoleService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
public class OpenController
{

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/createnewuser",
        consumes = {"application/json"},
        produces = {"application/json"})
    public ResponseEntity<?> addSelf(
        HttpServletRequest httpServletRequest,
        @Valid
        @RequestBody
            UserMinimum newminuser)
        throws
        URISyntaxException
    {
        User newuser = new User();

        newuser.setUsername(newminuser.getUsername());
        newuser.setPassword(newminuser.getPassword());
        newuser.setPrimaryemail(newminuser.getPrimaryemail());

        Set<UserRoles> newRoles = new HashSet<>();
        newRoles.add(new UserRoles(newuser,
            roleService.findByName("user")));
        newuser.setRoles(newRoles);

        newuser = userService.save(newuser);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromUriString(httpServletRequest.getServerName() + ":" + httpServletRequest.getLocalPort() + "/users/user/{userId}")
            .buildAndExpand(newuser.getUserid())
            .toUri();
        responseHeaders.setLocation(newUserURI);


        RestTemplate restTemplate = new RestTemplate();
        String requestURI = "http://localhost" + ":" + httpServletRequest.getLocalPort() + "/login";

        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(acceptableMediaTypes);
        headers.setBasicAuth(System.getenv("OAUTHCLIENTID"),
            System.getenv("OAUTHCLIENTSECRET"));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type",
            "password");
        map.add("scope",
            "read write trust");
        map.add("username",
            newminuser.getUsername());
        map.add("password",
            newminuser.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
            headers);

        String theToken = restTemplate.postForObject(requestURI,
            request,
            String.class);

        return new ResponseEntity<>(theToken,
            responseHeaders,
            HttpStatus.CREATED);
    }


    @ApiIgnore
    @GetMapping("favicon.ico")
    public void returnNoFavicon()
    {

    }
}
