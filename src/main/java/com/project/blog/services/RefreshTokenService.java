package com.project.blog.services;

import com.project.blog.entities.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken createRefreshToken(String userName);
    public RefreshToken verifyRefreshToken(String refreshToken);
}
