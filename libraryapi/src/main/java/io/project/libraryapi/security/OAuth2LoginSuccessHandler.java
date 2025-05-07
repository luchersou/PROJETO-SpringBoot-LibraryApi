package io.project.libraryapi.security;

import io.project.libraryapi.model.User;
import io.project.libraryapi.model.UserRole;
import io.project.libraryapi.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String STANDARD_PASSWORD = "123";

    private final UserService service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        // Extracts the authenticated user's email from the OAuth2 login session.
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        // Sets a custom authenticated user in the SecurityContext after finding them by email.
        User user = service.findByEmail(email);

        if(user == null){
            user = userRegistrationDataBase(email);
        }

        authentication = new CustomAuthentication(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    // Method to register the user who logged in
    private User userRegistrationDataBase(String email) {
        User user;
        user = new User();
        user.setLogin(email);
        user.setEmail(email);
        user.setPassword(STANDARD_PASSWORD);
        user.setUserRole(UserRole.USER);
        service.save(user);
        return user;
    }
}
