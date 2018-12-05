package com.dreamwork.art.config;

import com.dreamwork.art.config.filters.APIKeyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@Order(1)
public class PublicConfig extends WebSecurityConfigurerAdapter {
    @Value("${appapi.auth-token-header}")
    private String principalRequestHeader;

    @Value("${appapi.auth-token}")
    private String principalRequestValue;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        APIKeyFilter filter = new APIKeyFilter(principalRequestHeader);

        filter.setAuthenticationManager(authentication -> {
            if (!principalRequestValue.equals(authentication.getPrincipal()))
                throw new BadCredentialsException("The API key was not found or not the expected value.");

            authentication.setAuthenticated(true);
            return authentication;
        });

        http.antMatcher("/api/public/**")
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                    .and()
                .csrf().disable()
                .addFilter(filter).authorizeRequests().anyRequest().authenticated()
                    .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
