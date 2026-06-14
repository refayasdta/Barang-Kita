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
            .cors(cors -> cors.disable()) 
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. PUBLIC ASSETS (JS, Images, etc. so the browser doesn't get 403s)
                .requestMatchers("/js/**", "/images/**", "/assets/**", "/favicon.ico").permitAll()
                
                // 2. AUTHENTICATION ENDPOINTS
                .requestMatchers("/api/auth/**").permitAll()
                
                // 3. PUBLIC PAGES
                .requestMatchers("/login", "/register", "/home", "/item-detail", "/cart", "/checkout", 
                                 "/profile", "/edit-profile", "/order-history", "/tentang-kami", 
                                 "/final-checkout", "/admin-dashboard", "/admin-orders").permitAll()
                
                // 4. ITEMS (Public GET)
                .requestMatchers(HttpMethod.GET, "/api/items/**").permitAll()
                
                // 5. RESTRICTED ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/api/items/**").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/items/**").hasAuthority("Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/items/**").hasAuthority("Admin")

                .requestMatchers(HttpMethod.GET, "/api/carts/**").hasAnyAuthority("User", "Admin")
                .requestMatchers(HttpMethod.POST, "/api/carts/**").hasAnyAuthority("User", "Admin")
                .requestMatchers(HttpMethod.PUT, "/api/carts/**").hasAuthority("User")
                .requestMatchers(HttpMethod.DELETE, "/api/carts/**").hasAuthority("User")

                .requestMatchers(HttpMethod.GET, "/api/orders/user/**").hasAuthority("User")
                .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAuthority("User")
                .requestMatchers(HttpMethod.GET, "/api/orders/all").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAuthority("Admin")
                .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyAuthority("Admin", "User")

                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority("User", "Admin")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyAuthority("User", "Admin")

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