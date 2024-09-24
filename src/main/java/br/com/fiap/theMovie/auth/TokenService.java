package br.com.fiap.theMovie.auth;

import br.com.fiap.theMovie.models.User;
import br.com.fiap.theMovie.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final UserRepository userRepository;
    private Algorithm algorithm;

    public TokenService(UserRepository userRepository, @Value("${jwt.secret}") String secret) {
        this.userRepository = userRepository;
        algorithm = Algorithm.HMAC256(secret);
    }

    public Token create(User user){
        var expiresAt = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.ofHours(-3));

        String token = JWT.create()
                .withIssuer("themovie")
                .withSubject(user.getEmail())
                .withClaim("role", "admin")
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new Token(token);
    }

    public User getUserFromToken(String token) {
        var email =JWT.require(algorithm)
                .withIssuer("themovie")
                .build()
                .verify(token)
                .getSubject();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

    }

}
