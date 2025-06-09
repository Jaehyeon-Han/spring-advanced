package org.example.expert.config;

import org.example.expert.global.security.JwtUtil;
import org.example.expert.global.interceptor.AdminInterceptor;
import org.example.expert.domain.auth.exception.NotAuthorizedException;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AdminInterceptorTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    void interceptor_shouldLog_whenUserRoleIsAdmin() {
        // given
        String adminToken = jwtUtil.createToken(1L, "user@email.com", UserRole.ADMIN);
        AdminInterceptor adminInterceptor = new AdminInterceptor(jwtUtil);
        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.addHeader("Authorization", adminToken);
        httpRequest.setRequestURI("http://fake-uri.com");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        // when
        boolean result;
        try {
            result = adminInterceptor.preHandle(httpRequest, httpResponse, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // then
        assertTrue(result);
    }

    @Test
    void interceptor_shouldThrowNotAuthorizedException_whenUserRoleIsNotAdmin() {
        // given
        String userToken = jwtUtil.createToken(1L, "user@email.com", UserRole.USER);
        AdminInterceptor adminInterceptor = new AdminInterceptor(jwtUtil);
        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.addHeader("Authorization", userToken);
        httpRequest.setRequestURI("http://fake-uri.com");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        // when-then
        try {
            assertThatThrownBy(() -> adminInterceptor.preHandle(httpRequest, httpResponse, null))
                .isInstanceOf(NotAuthorizedException.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
