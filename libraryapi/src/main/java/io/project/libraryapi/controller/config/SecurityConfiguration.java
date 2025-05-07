package io.project.libraryapi.controller.config;

import io.project.libraryapi.security.CustomUserDetailsService;
import io.project.libraryapi.security.OAuth2LoginSuccessHandler;
import io.project.libraryapi.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(http -> {
                    http.requestMatchers("/login/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/users/**").permitAll();
                    http.anyRequest().authenticated();
                })
                .oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer.successHandler(oAuth2LoginSuccessHandler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userservice){

//        UserDetails user1 = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("1234"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("4321"))
//                .roles("ADMIN")
//                .build();

        return new CustomUserDetailsService(userservice);
    }

/* This is necessary if you choose not to put "ROLE_" in CustomAuthentication class. Both do the same thing
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }
 */
}
