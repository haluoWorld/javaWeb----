package com.liule.booksys.config;
import com.liule.booksys.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 配置 CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:5173"); // 允许所有来源
        corsConfig.addAllowedMethod("*"); // 允许所有 HTTP 方法
        corsConfig.addAllowedHeader("*"); // 允许所有请求头
        corsConfig.setAllowCredentials(true); // 允许携带凭证

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // 对所有路径生效
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasAnyRole("admin") // 仅管理员可访问
                        .requestMatchers("/user/**").hasAnyRole("user", "admin") // 用户或管理员可访问
                        .requestMatchers("/auth/**").permitAll() // 开放认证接口
                        //.requestMatchers("/book/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态 JWT
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 显式设置CORS配置
                .build();
    }
}