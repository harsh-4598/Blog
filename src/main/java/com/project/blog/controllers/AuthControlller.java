package com.project.blog.controllers;

import com.project.blog.dtos.AuthRequest;
import com.project.blog.dtos.JwtAuthResponse;
import com.project.blog.dtos.RefreshTokenRequest;
import com.project.blog.entities.RefreshToken;
import com.project.blog.entities.User;
import com.project.blog.security.CustomUserDetailService;
import com.project.blog.services.RefreshTokenService;
import com.project.blog.utility.JwtHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "AuthController", description = "APIs for authentication")
public class AuthControlller {
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody AuthRequest authRequest) {
       Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
       if (authenticate.isAuthenticated()) {
           UserDetails userDetails = customUserDetailService.loadUserByUsername(authRequest.getUsername());
           JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
           jwtAuthResponse.setToken(jwtHelper.generateToken(userDetails));
           jwtAuthResponse.setRefreshToken(refreshTokenService.createRefreshToken(userDetails.getUsername()).getRefreshToken());
           return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
       } else {
           throw new BadCredentialsException("Invalid username password");
       }
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refreshJwtToken(@RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken =  refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        UserDetails userDetails = customUserDetailService.loadUserByUsername(refreshToken.getUser().getEmail());
        String jwtToken =  jwtHelper.generateToken(userDetails);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(jwtToken);
        jwtAuthResponse.setRefreshToken(refreshToken.getRefreshToken());
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

}
