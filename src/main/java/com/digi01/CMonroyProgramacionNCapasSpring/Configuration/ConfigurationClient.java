
package com.digi01.CMonroyProgramacionNCapasSpring.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class ConfigurationClient {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login",
                    "/guardarToken",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/usuario/**"
                ).permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable());

        return http.build();
    }
}

