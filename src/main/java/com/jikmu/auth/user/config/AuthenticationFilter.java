package com.jikmu.auth.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jikmu.auth.jwt.JwtUtil;
import global.CommonResponseDto;
import global.config.BaseException;
import global.exception.CustomApiException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            if (request.getRequestURI().equals("/login") ||
                    request.getRequestURI().equals("/signup")
            ) {
                log.info("요청 제외 필터");
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");

            log.info("Authorization header: {}", authHeader);

            if (authHeader != null) {
                authHeader = jwtUtil.substringToken(authHeader);
                jwtUtil.validateToken(authHeader);
                Claims claims = jwtUtil.getUserInfoFromToken(authHeader);
                String username = claims.getSubject();
                String userRole = claims.get("role", String.class);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_" + userRole)));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("인증 성공: userId={}, role={}", username, userRole);
            } else {
                log.warn("유효하지 않은 토큰 또는 토큰 없음");
            }

            filterChain.doFilter(request, response);
        }catch (CustomApiException e) {

            response.setStatus(e.getStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            CommonResponseDto<Object> errorResponse = new CommonResponseDto<>(e.getException());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
            log.error("JWT 인증 필터 처리 중 예외 발생", e);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json;charset=UTF-8");

            objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(
                    new CommonResponseDto<>(BaseException.INVALID_INPUT));
            response.getWriter().write(json);
        }

    }
}
