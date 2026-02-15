package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;
    private final CurrentUserService self;

    public CurrentUserService(UserRepository userRepository, @Lazy CurrentUserService self) {
        this.userRepository = userRepository;
        this.self = self;
    }

    public User requireCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("Unauthenticated");
        }
        String email = authentication.getName();
        return self.getByEmail(email);
    }

    @Cacheable(value = "users", key = "#email")
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found for email: " + email));
    }
}
