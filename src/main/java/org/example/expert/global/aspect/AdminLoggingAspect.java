package org.example.expert.global.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.global.security.JwtUtil;
import org.example.expert.global.util.DateTimeFormattingUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AdminLoggingAspect {

    
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    // 어노테이션 붙이고, @annotation 활용하는 방법도 가능할 듯
    @Pointcut(
        "execution(public void org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))" +
            "|| execution(public void org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))"
    )
    private void adminMethods() {
    }

    @Around("adminMethods()")
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 사용자 id 로깅
        String bearerToken = request.getHeader("Authorization");
        String jwt = jwtUtil.substringToken(bearerToken);
        log.info("Requester ID: {}", jwtUtil.getUserId(jwt));

        // 요청 시각 및 URL 로깅
        log.info("Requested URI: {}", request.getRequestURI());
        log.info("Requested time: {}", DateTimeFormattingUtil.formattedNow(TIME_FORMAT));

        logRequestBody(joinPoint);

        try {
            Object result = joinPoint.proceed();
            // 응답 본문 로깅 (현재는 void 여서 null 출력)
            log.info("Response body: {}", objectMapper.writeValueAsString(result));
        } catch (Throwable e) {
            log.info("Error occurred executing admin methods");
            throw e;
        }
    }

    /**
     * 컨트롤러 메소드 매개변수에 @RequestBody 가 있는 경우, 호출 시 인자를 로깅한다.
     *
     * @param joinPoint: AOP 에서의 ProceedingJoinPoint
     */

    private void logRequestBody(ProceedingJoinPoint joinPoint) throws JsonProcessingException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 파라미터 어노테이션 배열 (각 파라미터마다 여러 어노테이션 가능)
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        boolean hasBody = false;
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType() == RequestBody.class) {
                    hasBody = true;
                    Object arg = args[i];
                    log.info("Request body: {}", objectMapper.writeValueAsString(arg));
                    break;
                }
            }
        }

        if(!hasBody) {
            log.info("Request body: null");
        }
    }

    /*
    다음을 로깅해야 한다.
    - 요청한 사용자의 ID -> 토큰 내부에 ✅
    - 요청 본문(`RequestBody`) -> 요청 내부, Jackson 사용 ✅
    - 응답 본문(`ResponseBody`) -> 응답 내부, Jackson 사용 ✅

    - API 요청 시각 -> 유틸 클래스 ✅
    - API 요청 URL -> 요청에서 추출 ✅
     */
}