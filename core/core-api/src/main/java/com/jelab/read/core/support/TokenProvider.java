package com.jelab.read.core.support;

import com.jelab.read.core.enums.SocialType;
import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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

    public String createAccessToken(Long memberId, String email, String name, SocialType socialType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .header()
            .type("JWT")
            .and()
            .subject(String.valueOf(memberId))
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

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);

        }
        catch (SecurityException | MalformedJwtException | SignatureException e) {
            throw new CoreException(ErrorType.JWT_INVALID, e.getMessage(), e);
        }
        catch (ExpiredJwtException e) {
            throw new CoreException(ErrorType.JWT_EXPIRE, e.getMessage(), e);
        }
        catch (UnsupportedJwtException e) {
            throw new CoreException(ErrorType.JWT_UNSUPPORTED, e.getMessage(), e);
        }
        catch (IllegalArgumentException e) {
            throw new CoreException(ErrorType.JWT_INVALID,
                    "JWT tokens strings must contain exactly 2 period characters.", e);
        }
    }

}
