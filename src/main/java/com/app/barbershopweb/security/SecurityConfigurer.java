package com.app.barbershopweb.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };

    public SecurityConfigurer(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .mvcMatchers(SWAGGER_WHITELIST).permitAll()

                .mvcMatchers(HttpMethod.POST, "/users").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/users/avatars/**").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                .mvcMatchers(HttpMethod.DELETE, "/barbershops/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/barbershops").hasRole("ADMIN")

                .mvcMatchers(HttpMethod.POST, "/orders").hasAnyRole("BARBER", "ADMIN")
                .mvcMatchers("/orders/reservations").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/orders/**").hasAnyRole("BARBER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/orders/**").hasAnyRole("BARBER", "ADMIN")

                .anyRequest().authenticated()
                .and().formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}
