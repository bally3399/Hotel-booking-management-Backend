package topg.bimber_user_service.security.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import topg.bimber_user_service.security.filter.CustomAuthorizationFilter;
import topg.bimber_user_service.security.filter.CustomUsernamePasswordAuthenticationFilter;

import java.util.List;


@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;
    private final CustomAuthorizationFilter customAuthorizationFilter;
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        var authenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager);
//        authenticationFilter.setFilterProcessesUrl("/api/v1/auth");
//            return httpSecurity.csrf(c->c.disable())
//                    .cors(c->c.disable())
//                    .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                    .addFilterAt(authenticationFilter, BasicAuthenticationFilter.class)
//                    .addFilterBefore(customAuthorizationFilter, CustomUsernamePasswordAuthenticationFilter.class)
//                    .authorizeHttpRequests(c->c.requestMatchers("/api/v1/auth").permitAll()
//                            .requestMatchers("/api/v1/users/register").permitAll()
//                            .requestMatchers("/api/v1/admin/register").permitAll()
//                            .requestMatchers("/api/v1/users/**").hasAnyAuthority("USER")
//                            .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN")
//
//                    )
//                    .build();
//
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        var authenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth");
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(request -> {
                    var cors = new org.springframework.web.cors.CorsConfiguration();
                    cors.setAllowedOrigins(List.of("http://localhost:3000"));
                    cors.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                }))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(authenticationFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(customAuthorizationFilter, CustomUsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/v1/auth").permitAll()
                        .requestMatchers("/api/v1/users/register").permitAll()
                        .requestMatchers("/api/v1/admin/register").permitAll()
                        .requestMatchers("/api/v1/users/**").hasAnyAuthority("USER")
                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .build();
    }


}

