package com.task.application.request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login", "/register"
    };

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        User.UserBuilder builder = User.withDefaultPasswordEncoder();
        UserDetails admin = builder.username("admin").password("admin").roles("ADMIN").build();
        UserDetails user = builder.username("user").password("user").roles("USER").build();
        UserDetails operator = builder.username("operator").password("operator").roles("OPERATOR").build();
        return new InMemoryUserDetailsManager(user,admin, operator);
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                        authz
                                .mvcMatchers(AUTH_WHITELIST).permitAll()
                                .mvcMatchers("/requests/**").hasAnyRole("USER", "OPERATOR")
                                .mvcMatchers("/users/**").hasAnyRole("ADMIN")

                )
                .formLogin()
                .defaultSuccessUrl("/swagger-ui/index.html", true)
                .and()
                .cors().and()
                .httpBasic(withDefaults())
                .logout()
                .logoutUrl("/perform_logout")
                .invalidateHttpSession(true)
                .deleteCookies(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        return http.build();
    }
}
