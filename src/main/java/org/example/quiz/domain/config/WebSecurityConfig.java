package org.example.quiz.domain.config;

import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationConfiguration authConfiguration;

    private final UserServiceImpl userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager manager = builder.build();

        http
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/quiz/**").authenticated()
                            .requestMatchers("/question/**").authenticated()
                            .requestMatchers("/option/**").authenticated()
                            .requestMatchers("/registration").permitAll()
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/css/**").permitAll()
                            .requestMatchers("/images/**").permitAll()
                            .anyRequest().permitAll();
                })
                .formLogin(login -> {
                    login.loginPage("/custom-login")
                            .loginProcessingUrl("/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/", true)
                            .permitAll();
                })
                .logout(logout -> {
                    logout.invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .clearAuthentication(true)
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .logoutSuccessUrl("/");
                })
                .exceptionHandling(handling -> {
                    handling
                            .accessDeniedPage("/403");
                })
                .rememberMe(rememberMe -> {
                    rememberMe.alwaysRemember(true)
                            .key("asdqwafasfasfasf")
                            .userDetailsService(userService)
                            .tokenValiditySeconds(7200);
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(manager);

        return http.build();
    }
}
