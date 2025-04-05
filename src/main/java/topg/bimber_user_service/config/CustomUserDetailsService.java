//package topg.bimber_user_service.config;
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import topg.bimber_user_service.models.Admin;
//import topg.bimber_user_service.models.Role;
//import topg.bimber_user_service.models.User;
//import topg.bimber_user_service.repository.AdminRepository;
//import topg.bimber_user_service.repository.UserRepository;
//
//import java.util.Collections;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepository;
//    private final AdminRepository adminRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository, AdminRepository adminRepository) {
//        this.userRepository = userRepository;
//        this.adminRepository = adminRepository;
//    }
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserDetails userDetails = adminRepository.findByEmail(username);
//        if(userDetails == null) userDetails = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User or Admin not found: " + username));
//        if(userDetails == null) throw new UsernameNotFoundException("User Not Found");
//        return userDetails;
//
//    }
//}
