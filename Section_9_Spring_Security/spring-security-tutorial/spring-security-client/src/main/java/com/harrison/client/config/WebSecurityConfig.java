package com.harrison.client.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {
            "/hello/**",
            "/register/**",
            "/verifyRegistration/**",
            "/resendVerifyToken/**",
            "/savePassword/**",
            "/resetPassword/**"
    };

    @Bean // So we can autowire it
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    @SuppressWarnings(value = {"deprecated", "removal"})
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
//                .antMatchers(WHITE_LIST_URLS).permitAll(); // Removed in later patches
                .requestMatchers(HttpMethod.GET, WHITE_LIST_URLS).permitAll()
                .requestMatchers(HttpMethod.POST, WHITE_LIST_URLS).permitAll();

        return httpSecurity.build();
    }
}
