package com.dreamwork.art.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@Order(2)
public class PrivateConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                    .antMatchers("/api/private/**", "/monitoring/**")
                .and()
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()) // <--- настроить
                    .and()
                .csrf().disable()
                .authorizeRequests().anyRequest().hasRole("ADMIN")
                .and()
                    .httpBasic().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("admin_realm");
        return entryPoint;
    }
}
