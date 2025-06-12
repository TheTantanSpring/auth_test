package com.jikmu.auth.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jikmu.auth.jwt.JwtUtil;
import global.CommonResponseDto;
import global.config.BaseException;
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
                jwtUtil.validateToken(authHeader);
                Claims claims = jwtUtil.getUserInfoFromToken(authHeader);
                UUID userId = UUID.fromString(claims.getSubject());
                String role = claims.get("role", String.class);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("인증 성공: userId={}, role={}", userId, role);
            } else {
                log.warn("유효하지 않은 토큰 또는 토큰 없음");
            }

            filterChain.doFilter(request, response);
        }catch (Exception e) {

//            response.setStatus(e.getStatus().value());
//            response.setContentType("application/json;charset=UTF-8");
//
//            CommonResponse<Object> errorResponse = new CommonResponse<>(e.getServiceCode());
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(errorResponse);
//            response.getWriter().write(json);
            log.error("JWT 인증 필터 처리 중 예외 발생", e);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json;charset=UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(
                    new CommonResponseDto<>(BaseException.INVALID_INPUT));
            response.getWriter().write(json);
        }

    }
}
