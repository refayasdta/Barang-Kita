package com.barangkita.barang_kita.config;

import com.barangkita.barang_kita.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("/assets/**").permitAll()

                .requestMatchers("/login", "/register").permitAll()
                
                .requestMatchers("/api/auth/**").permitAll()
                
                .requestMatchers(HttpMethod.GET, "/api/items/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/items/**").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/items/**").hasAuthority("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/items/**").hasAuthority("Admin")

                .requestMatchers("/api/carts/**").hasAuthority("User")

                .requestMatchers(HttpMethod.GET, "/api/orders/user/**").hasAuthority("User")
                .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAuthority("User")

                .requestMatchers(HttpMethod.GET, "/api/orders/all").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAuthority("Admin")
                .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyAuthority("Admin", "User")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}