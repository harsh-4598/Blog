package com.project.blog.services.impl;

import com.project.blog.entities.RefreshToken;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.exceptions.ValidationException;
import com.project.blog.repositories.RefreshTokenRepo;
import com.project.blog.repositories.UserRepo;
import com.project.blog.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final int refreshTokenValidity = 60*2*1000;
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public RefreshToken createRefreshToken(String userName) {
        User user = userRepo.findByEmail(userName);
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            refreshToken = new RefreshToken(UUID.randomUUID().toString(), Instant.now().plusMillis(refreshTokenValidity), user);
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }
        user.setRefreshToken(refreshToken);
        refreshTokenRepo.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
       RefreshToken refreshToken1 =  refreshTokenRepo.findByRefreshToken(refreshToken).orElseThrow(() -> new ValidationException("Invalid Refresh Token"));
        if (refreshToken1.getExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(refreshToken1);
            throw new ValidationException("Refresh Token Expired");
        }
        return refreshToken1;
    }
}
