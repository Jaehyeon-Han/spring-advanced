package org.example.expert.config;

import io.jsonwebtoken.Claims;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.global.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    void extractRoleTest() {
        String bearerToken = jwtUtil.createToken(1L, "user@email.com", UserRole.ADMIN);
        String token = jwtUtil.substringToken(bearerToken);
        Claims claims = jwtUtil.extractClaims(token);
        System.out.println(claims.get("userRole", UserRole.class));
    }

    @Test
    void extractUserIdTest() {
        String bearerToken = jwtUtil.createToken(1L, "user@email.com", UserRole.ADMIN);
        String token = jwtUtil.substringToken(bearerToken);
        long userId = jwtUtil.getUserId(token);
        assertThat(userId).isEqualTo(1L);
    }
}
