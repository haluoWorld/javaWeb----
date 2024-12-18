package com.liule.booksys.security;
import com.liule.booksys.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = JwtUtils.parseToken(token); // 验证 JWT
                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                // 检查解析结果是否有效
                if (username == null || role == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                request.setAttribute("role", role);
                request.setAttribute("username", username);

                // 将用户信息和权限注入到 SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null, // 没有密码
                        Collections.singletonList(new SimpleGrantedAuthority(prefixedRole)) // 设置权限
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error("JWT 验证失败: {}"+e.getMessage());
                return;
            }
        }

        // 调用下一个过滤器
        chain.doFilter(request, response);
    }
}
