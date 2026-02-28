package com.jelab.read.core.support;


import com.jelab.read.core.enums.SocialType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public TokenProvider(@Value("${jwt.secret.key}") String secretKeyString,
                         @Value("${jwt.expire-length}") long validityInMilliseconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createAccessToken(String socialId, String email, String name, SocialType socialType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(socialId)
                .claim("email", email)
                .claim("name", name)
                .claim("socialType", socialType.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken() {
        return UUID.randomUUID().toString();
    }

}
