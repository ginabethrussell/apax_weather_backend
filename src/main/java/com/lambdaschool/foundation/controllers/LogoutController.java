package com.lambdaschool.foundation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class LogoutController
{

    @Autowired
    private TokenStore tokenStore;

    @GetMapping(value = {"/oauth/revoke-token", "/logout"},
        produces = "application/json")
    public ResponseEntity<?> logoutSelf(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null)
        {
            // find the token
            String tokenValue = authHeader.replace("Bearer",
                "")
                .trim();
            // and remove it!
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
