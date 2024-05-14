package com.project.blog.repositories;

import com.project.blog.entities.Role;
import com.project.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    Set<Role> findByUsers(User user);
    Role findByRole(String role);
}
