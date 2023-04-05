package com.smartmanager.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
   @Bean
    public UserDetailsService userDetailsService(){
        return new CustomerDetailsService();
    }
   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder();
   }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests()
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .requestMatchers("/user/**")
                .hasRole("USER")
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/signin")
                .defaultSuccessUrl("/user/index")
                .failureUrl("/login-fail")
                .and().csrf().disable();



        System.out.println("HI");
        return http.build();
    }

}
