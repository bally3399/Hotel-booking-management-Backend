package topg.bimber_user_service.security.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import topg.bimber_user_service.security.filter.CustomAuthorizationFilter;
import topg.bimber_user_service.security.filter.CustomUsernamePasswordAuthenticationFilter;


@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        var authenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth");
            return httpSecurity.csrf(c->c.disable())
                    .cors(c->c.disable())
                    .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterAt(authenticationFilter, BasicAuthenticationFilter.class)
                    .addFilterBefore(customAuthorizationFilter, CustomUsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(c->c.requestMatchers("/api/v1/auth").permitAll()
                            .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN")
                            .requestMatchers("/api/v1/user/**").hasAnyAuthority("ADMIN")

                    )
                    .build();

    }

}

