package com.project.blog.exceptions;

import com.project.blog.dtos.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        return new ResponseEntity<>(new ApiResponse(message, false), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiResponse> validationExceptionHandler(ValidationException ex) {
        return new ResponseEntity<>(new ApiResponse(ex.message, false), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentValidExceptionHandler(MethodArgumentNotValidException ex) {
        Map<String, String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParamsExceptionHandler(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(new ApiResponse("Param "+ex.getParameterName()+" is missing"), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> userNotFoundExceptionHandler(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentialsExceptionHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(new ApiResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Access Denied!", HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = io.jsonwebtoken.MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedTokenException(io.jsonwebtoken.MalformedJwtException ex) {
        return new ResponseEntity<>("Access Denied, passed Malformed token", HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleTokenExpirationException(ExpiredJwtException ex) {
        return new ResponseEntity<>("Access Denied, token expired", HttpStatus.UNAUTHORIZED);
    }
}
