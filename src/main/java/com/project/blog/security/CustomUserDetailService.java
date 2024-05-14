package com.project.blog.security;

import com.project.blog.entities.User;
import com.project.blog.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =this.userRepo.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("User "+username + "not found");
        return new UserInfoDetails(user);
    }
}
