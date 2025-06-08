package pl.edu.streamfinder.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.edu.streamfinder.user.UserService;

import java.util.Date;

import static java.util.UUID.randomUUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final long EXPIRATION_MS = 1000 * 60 * 60 * 24;
    private final UserService userService;

    public JwtService(UserService userService) {
        this.userService = userService;
    }

    public String generateToken(Authentication authentication) {
        String email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("Email attribute is missing in the authentication principal");
        }

        if (userService.findByEmail(email) == null) {
            pl.edu.streamfinder.user.User newUser = new pl.edu.streamfinder.user.User();
            newUser.setEmail(email);
            newUser.setPasswordHash(randomUUID().toString());

            userService.createUser(newUser);
        }

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String email = Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody().getSubject();

        UserDetails user = User.withUsername(email).password("").authorities("USER").build();

        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }
}

