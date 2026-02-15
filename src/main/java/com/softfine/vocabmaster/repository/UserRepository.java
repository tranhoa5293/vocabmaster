package com.softfine.vocabmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.softfine.vocabmaster.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
