package pl.edu.streamfinder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final String redirectUri;

    public CustomOAuth2SuccessHandler(JwtUtils jwtUtils, @Value("${frontend.redirect-uri}") String redirectUri) {
        this.jwtUtils = jwtUtils;
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String token = jwtUtils.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // ustaw na true w HTTPS
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        response.sendRedirect(redirectUri);
    }
}
