package com.abhi.docman.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhi.docman.model.User;

public interface UserRepo extends JpaRepository<User,Long> {

    User findByEmail(String email);
    
} 