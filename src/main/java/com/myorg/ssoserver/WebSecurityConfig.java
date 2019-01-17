package com.myorg.ssoserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 *
 * @author veeresh
 */
@Configuration
@EnableWebSecurity
@Order(-20)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public CookieSerializer cookieSerializer() {
        
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SSO_SEESION");
        serializer.setCookiePath("/");
        return serializer;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .roles("ADMIN")
                .password("{noop}password");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().failureUrl("/login?error").permitAll()
                .and()
                .logout().logoutSuccessUrl("/login?logout").permitAll()
                .and()
                .requestMatchers().antMatchers("/oauth/authorize", "/", "/login", "/logout")
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/logout")
                .permitAll()
                .anyRequest().authenticated();
    }
}
