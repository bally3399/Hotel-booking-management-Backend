//package topg.bimber_user_service.service;
//
//import lombok.AllArgsConstructor;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import topg.bimber_user_service.dto.requests.LoginRequest;
//import topg.bimber_user_service.dto.responses.LoginResponse;
//import topg.bimber_user_service.exceptions.UserNotFoundException;
//import topg.bimber_user_service.models.Admin;
//import topg.bimber_user_service.models.User;
//import topg.bimber_user_service.repository.AdminRepository;
//import topg.bimber_user_service.repository.UserRepository;
//
//@Service
//@AllArgsConstructor
//public class AuthService {
//
//    private AdminRepository admins;
//
//    private UserRepository users;
//
//    private AuthenticationManager authManager;
//
//
//    public LoginResponse login(LoginRequest request) {
//        Admin admin = admins.findByEmail(request.getEmail());
//        LoginResponse response = null;
//        if (admin != null) {
//            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//            var jwt = jwtService.generateToken(admin);
//            admins.save(admin);
//            LoginResponse loginResponse = new LoginResponse();
//            loginResponse.setRole(admin.getRole());
//            loginResponse.setMessage("Login Successful");
//            loginResponse.setJwtToken(jwt);
//            loginResponse.setUsername(admin.getUsername());
//            response = loginResponse;
//        }
//        else{
//            System.out.println(request.getEmail());
//            User user = users.findByEmail(request.getEmail()).orElseThrow(()-> new UserNotFoundException("User Not Found"));
//            System.out.println(user);
//            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//            var jwt = jwtService.generateToken(user);
//            var refreshFresh = jwtService.generateRefreshToken(user);
//            users.save(user);
//            LoginResponse loginResponse = new LoginResponse();
//            loginResponse.setRole(user.getRole());
//            loginResponse.setMessage("Login Successful");
//            loginResponse.setRefreshToken(refreshFresh);
//            loginResponse.setJwtToken(jwt);
//            loginResponse.setUsername(user.getUsername());
//            response = loginResponse;
//        }
//        if(response == null) throw new IllegalArgumentException("InValid User Credentials");
//
//
//        return response;
//    }
//}
