package com.project.blog.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String refreshToken;
    private Instant expiry;
    @OneToOne
    private User user;

    public RefreshToken(String refreshToken, Instant expiry, User user) {
        this.refreshToken = refreshToken;
        this.expiry = expiry;
        this.user = user;
    }
}
