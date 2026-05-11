package com.barangkita.barang_kita.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Look for the "Authorization" header in the incoming request
        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        // 2. Check if the header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Remove "Bearer " to get just the token
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                System.out.println("Invalid Token");
            }
        }

        // 3. If we found an email and the user isn't already logged in to the Spring context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            if (jwtUtil.validateToken(jwt, email)) {
                // Extract the role from the token
                String role = jwtUtil.extractRole(jwt);
                
                // Tell Spring Security: "This user is valid and here is their role"
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 4. Pass the request along to the next step
        filterChain.doFilter(request, response);
    }
}