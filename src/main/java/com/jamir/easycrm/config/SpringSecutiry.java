package com.jamir.easycrm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecutiry {
    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http){
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(request -> request
            	.requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.GET, "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuario/cadastro").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuario/cadastro").permitAll()
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/clientes", true)                
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .permitAll()
            ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
            );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}