package com.app.barbershopweb.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };


    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers("/**").permitAll()
//                .antMatchers("/users/**").hasAnyAuthority("user")
//                .antMatchers("/barbershops/**").hasAuthority("user")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery(
                    "SELECT username, password, enabled "
                    + "FROM user_credentials "
                    + "WHERE username = ?"
                )
                .authoritiesByUsernameQuery(
                    "SELECT user_credentials.username, users.role "
                     + "FROM users JOIN user_credentials "
                     + "ON users.user_id = user_credentials.user_id "
                     + "WHERE username = ?"
                );
    }
}
