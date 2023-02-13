package com.example.instaCat.repository;

import com.example.instaCat.entity.Post;
import com.example.instaCat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreateDate();

    List<Post> findAllByUserOrderByCreateDateDesc(User user);

    Optional<Post> findPostByIdAndUser(Long id, User user);
}

