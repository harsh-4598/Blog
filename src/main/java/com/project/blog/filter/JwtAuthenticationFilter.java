package com.project.blog.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.blog.dtos.ApiResponse;
import com.project.blog.exceptions.ValidationException;
import com.project.blog.security.CustomUserDetailService;
import com.project.blog.utility.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (ExpiredJwtException ex) {
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("JWT token has expired");
                return;
            } catch (MalformedJwtException | io.jsonwebtoken.security.SignatureException ex) {
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("Invalid jwt token, passed malformed token");
                return;
            } catch (Exception ex) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("Access Denied");
                return;
            }
        } else {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write("Access denied, invalid request, provide token");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //fetch user detail from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {
                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("Validation fails !!");
            }
        }
        filterChain.doFilter(request, response);
    }
    private static final List<String> skipFilterUrls = Arrays.asList("/api/user/create", "/api/auth/**", "/api/user/get/**", "/api/category/get/**", "/api/post/get/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger-resources", "/signIn");
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return skipFilterUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }
}
