//package topg.bimber_user_service.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@AllArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final CustomUserDetailsService userDetailsService;
//    private final JwtUtils jwtService;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String authHeader = request.getHeader("Authorization");
//        final String userEmail;
//        final String jwtToken;
//
//        if(authHeader == null || !authHeader.contains("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        jwtToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(jwtToken);
//        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//            if(jwtService.isTokenValid(jwtToken, userDetails)){
//                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//                userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(userToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
