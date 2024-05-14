package com.project.blog.repositories;

import com.project.blog.entities.Comment;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Post post, Pageable pageable);
    Page<Comment> findByUser(User user, Pageable pageable);
}
