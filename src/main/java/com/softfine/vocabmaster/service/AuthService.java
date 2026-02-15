package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.dto.UserResponse;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.repository.UserRepository;
import com.softfine.vocabmaster.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@AllArgsConstructor
public class AuthService {
    private final CurrentUserService currentUserService;

    public UserResponse getCurrentUser() {
        User user = currentUserService.requireCurrentUser();
        return new UserResponse(user.getId().toString(), user.getEmail(), user.getName());
    }
}
