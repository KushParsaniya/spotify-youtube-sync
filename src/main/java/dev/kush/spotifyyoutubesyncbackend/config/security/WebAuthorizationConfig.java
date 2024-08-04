package dev.kush.spotifyyoutubesyncbackend.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebAuthorizationConfig {

    private final CustomAuthEntryPoint customAuthEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/cache/**").hasRole("ADMIN")
                                .requestMatchers("/spotify/**", "/youtube/**",
                                        "sync/**", "/api/v1/sync/**", "/").permitAll()
                                .requestMatchers("/WEB-INF/views/*.jsp").permitAll()
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .exceptionHandling(e ->
                        e.accessDeniedHandler((req, res, ex) -> res.setStatus(403))
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
