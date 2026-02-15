package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.UserResponse;
import com.softfine.vocabmaster.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenController {
    private final AuthService authService;

    @GetMapping("/me")
    @ResponseBody
    public UserResponse getCurrentUser() {
        return authService.getCurrentUser();
    }
}
