package org.example.expert.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.global.security.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {

        /*
        필터에서 /admin 하위로의 접근 시 로그인 여부와 UserRole 의 ADMIN 여부를 확인하므로
        인터셉터에서 중복 확인할 필요가 없다.
        */

        log.info("Requested URI: {}", request.getRequestURI());
        log.info("Requested time: {}", getCurrentTimeInFormat());
        return true;
    }

    private String getCurrentTimeInFormat() {
        long requestTimeMillis = System.currentTimeMillis();
        LocalDateTime requestTime = Instant.ofEpochMilli(requestTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return requestTime.format(formatter);
    }
}
