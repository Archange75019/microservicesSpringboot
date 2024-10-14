package com.eshop.servicestock.security;

import com.eshop.servicestock.config.WebSecurityConfig;
import com.eshop.servicestock.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${user.service.url}")
    private String userServiceUrl;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                if (validateTokenWithServiceUser(jwt)) {
                    UserDetails userDetails = jwtUtil.loadUserByToken(jwt);
                    setAuthenticationForUser(request, userDetails);
                }
            } catch (Exception e) {
                logger.error("Token validation failed: ", e);  // Log the exception
            }
        }

        chain.doFilter(request, response);
    }

    private boolean validateTokenWithServiceUser(String token) {


        String serviceUserUrl = userServiceUrl;

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Ensure the token is sent in the proper format
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);  // Add the Authorization header

            HttpEntity<Map<String, String>> request = new HttpEntity<>(null, headers); // Send headers only, no body needed

            ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(serviceUserUrl, request, Boolean.class);

            return Boolean.TRUE.equals(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Error while validating token with service user: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while validating token with service user: ", e);
            return false;
        }
    }

    private void setAuthenticationForUser(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}