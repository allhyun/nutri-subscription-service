package project3.nutrisubscriptionservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project3.nutrisubscriptionservice.config.JwtProperties;
import project3.nutrisubscriptionservice.entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenProvider {
    @Autowired
    JwtProperties jwtProperties;

    public String create(UserEntity userEntity) {
        Date expridate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
                .setSubject(String.valueOf(userEntity.getId()))
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expridate)
                .compact();
    }

        //토큰을 검증하는 메소드
        public String validateAndGetUserId(String token){
            Claims claims =Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
    }
}
