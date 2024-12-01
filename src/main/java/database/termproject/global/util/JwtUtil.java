package database.termproject.global.util;

import database.termproject.domain.member.entity.Member;
import database.termproject.domain.member.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    @Value("${jwt.access-token-expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Member member){
        Map<String, Object> claims = createClaims(member);
        claims.put("type", "ACCESS_TOKEN");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(member.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }



    private Map<String, Object> createClaims(Member member) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", member.getId());
        claims.put("email", member.getEmail());
        claims.put("role", member.getRole());

        return claims;
    }


}
