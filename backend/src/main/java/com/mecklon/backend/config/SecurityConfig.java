package com.mecklon.backend.config;


import com.mecklon.backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(customizer -> customizer.disable());  // disables the use of csrf tokens used to protect sessions from cross side scripting
        http.authorizeHttpRequests(request -> request.requestMatchers("/signup", "/login", "/ws/**").permitAll()
                .anyRequest().authenticated());  // specifies that every request needs to be authenticated since we will be going for stateless rest
        //http.formLogin(Customizer.withDefaults());  // provides a default form to login with password username
        http.httpBasic(Customizer.withDefaults());  // specifies the form security basics to be the default ones
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // makes the session stateless
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exceptionHandlingConfigurer -> {
            exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
            });
        });
        return http.build();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
