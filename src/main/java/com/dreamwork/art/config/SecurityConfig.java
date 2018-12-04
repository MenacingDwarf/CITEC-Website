package com.dreamwork.art.config;

import com.dreamwork.art.config.filters.APIKeyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${appapi.auth-token-header}")
    private String principalRequestHeader;

    @Value("${appapi.auth-token}")
    private String principalRequestValue;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        APIKeyFilter filter = new APIKeyFilter(principalRequestHeader);

        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();

            if (!principalRequestValue.equals(principal)) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }

            authentication.setAuthenticated(true);
            return authentication;
        });

        http.
                cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and().
                antMatcher("/api/**").
                csrf().disable().

                sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }
}
