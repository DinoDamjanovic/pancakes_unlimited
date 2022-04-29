package com.dino_d.pancakes_unlimited.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/orders/reports/**").hasRole("STORE_OWNER")
                .antMatchers("/api/orders/**").hasRole("CUSTOMER")
                .antMatchers("/api/pancakes/**").hasRole("CUSTOMER")
                .antMatchers("/api/ingredients/**").hasRole("EMPLOYEE")
                .antMatchers("/api/categories/**").hasRole("EMPLOYEE")
                .anyRequest()
                .authenticated()
                .and().httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails customer = User.builder().username("customer")
                .password(passwordEncoder().encode("croz")).roles("CUSTOMER").build();
        UserDetails employee = User.builder().username("employee")
                .password(passwordEncoder().encode("palacinke")).roles("EMPLOYEE").build();
        UserDetails owner = User.builder().username("owner")
                .password(passwordEncoder().encode("store")).roles("STORE_OWNER").build();

        return new InMemoryUserDetailsManager(customer, employee, owner);
    }
}
