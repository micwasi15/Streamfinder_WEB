package pl.edu.streamfinder.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.edu.streamfinder.user.User;
import pl.edu.streamfinder.user.UserService;

import java.io.IOException;
import java.time.Duration;

import static java.util.UUID.randomUUID;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserService userService;
    private final String redirectUri;

    public CustomOAuth2SuccessHandler(JwtService jwtService, UserService userService, @Value("${cors.allowed.origins}") String redirectUri) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email attribute is missing in the authentication principal");
            return;
        }

        if (userService.findByEmail(email) == null) {
            User newUser = new pl.edu.streamfinder.user.User();
            newUser.setEmail(email);
            newUser.setPasswordHash(randomUUID().toString());

            userService.createUser(newUser);
        }
        String token = jwtService.generateToken(email);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        response.sendRedirect(redirectUri);
    }
}
