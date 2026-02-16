package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.dto.UserResponse;
import com.softfine.vocabmaster.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final CurrentUserService currentUserService;

    public UserResponse getCurrentUser() {
        User user = currentUserService.requireCurrentUser();
        return new UserResponse(user.getId().toString(), user.getEmail(), user.getName());
    }
}
