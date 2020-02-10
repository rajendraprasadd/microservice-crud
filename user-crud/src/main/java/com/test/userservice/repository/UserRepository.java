package com.test.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.userservice.entity.UserEntity;
 
@Repository
public interface UserRepository
        extends JpaRepository<UserEntity, Long> {
 
}
