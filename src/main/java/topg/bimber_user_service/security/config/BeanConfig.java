package topg.bimber_user_service.security.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import topg.bimber_user_service.security.provider.CustomAuthenticationProvider;
import topg.bimber_user_service.security.service.CustomUserDetailsService;

@Configuration
@AllArgsConstructor
public class BeanConfig {
    private final CustomUserDetailsService service;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider(service,passwordEncoder());
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> service.loadUserByUsername(username);
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        return  new CustomAuthenticationProvider(service,passwordEncoder());
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password");
        System.out.println("Encoded Password: " + encodedPassword);
    }

}
