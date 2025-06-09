package org.example.expert.config;

import io.jsonwebtoken.Claims;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.global.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    void extractRoleTest() {
        String token = jwtUtil.createToken(1L, "user@email.com", UserRole.ADMIN);
        String realToken = jwtUtil.substringToken(token);
        Claims claims = jwtUtil.extractClaims(realToken);
        System.out.println(claims.get("userRole", UserRole.class));
    }
}
