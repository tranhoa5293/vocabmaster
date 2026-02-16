package com.softfine.vocabmaster.security;

import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User)authentication.getPrincipal();
        if (oauth2User == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Login with OAuth2 provider failed:");
            return;
        }
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        String providerId = oauth2User.getAttribute("sub");


        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found from OAuth2 provider");
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                                .email(email)
                                .name(name == null || name.isBlank() ? email : name)
                                .picture(picture)
                                .provider("google")
                                .providerId(providerId == null ? "" : providerId)
                                .build()
                ));

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getName());
        String redirectUrl = frontendUrl + "?token=" + token;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}