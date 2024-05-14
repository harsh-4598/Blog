/*
package com.project.blog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.blog.dtos.AuthRequest;
import com.project.blog.dtos.JwtAuthResponse;
import com.project.blog.entities.RefreshToken;
import com.project.blog.entities.User;
import com.project.blog.repositories.RoleRepo;
import com.project.blog.security.CustomUserDetailService;
import com.project.blog.security.UserInfoDetails;
import com.project.blog.services.RefreshTokenService;
import com.project.blog.services.impl.RefreshTokenServiceImpl;
import com.project.blog.utility.JwtHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthControlller.class, includeFilters = {
        // to include JwtUtil in spring context
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtHelper.class)})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailService customUserDetailService;
    @MockBean
    private JwtHelper jwtHelper;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    RefreshTokenServiceImpl refreshTokenService;
    @MockBean
    RoleRepo roleRepo;
    private static UserDetails dummy;
    private static String jwtToken;
    private static RefreshToken refreshToken;

    @BeforeEach
    public void setUp() {
        dummy = new UserInfoDetails(User.builder().email("broker@gmail.com").password("Aa1@6578").roles(new HashSet<>()).build());
        jwtToken = "anyjwt";
        refreshToken = RefreshToken.builder().refreshToken("anyRefreshToken").build();
    }

    @Test
    public void createTokenTest() throws Exception {
        AuthRequest authRequest = AuthRequest.builder().username("broker@gmail.com").password("Aa1@6578").build();
        JwtAuthResponse jwtAuthResponse  = new JwtAuthResponse(jwtToken, refreshToken.getRefreshToken());

        ObjectMapper objectMapper = new ObjectMapper();
        // Convert object to JSON string
        String authRequestJsonString = objectMapper.writeValueAsString(authRequest);
        String jwtResponseJsonString = objectMapper.writeValueAsString(jwtAuthResponse);

        Authentication authentication = mock(Authentication.class);
        authentication.setAuthenticated(true);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(any())).thenReturn(authentication); // Failing here

        when(customUserDetailService.loadUserByUsername(eq("broker.com"))).thenReturn(dummy);
        when(jwtHelper.generateToken(dummy)).thenReturn(jwtToken);
        when(refreshTokenService.createRefreshToken(dummy.getUsername())).thenReturn(refreshToken);

        mockMvc.perform(post("/signIn")
                .content(authRequestJsonString)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}

*/
