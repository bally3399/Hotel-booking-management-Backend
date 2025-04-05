package topg.bimber_user_service.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import topg.bimber_user_service.security.utils.SecurityUtils;

import java.io.IOException;
import java.util.List;

import static topg.bimber_user_service.security.utils.SecurityUtils.JWT_PREFIX;


@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * 1a Retrieve request path
         * 1b if request path is a public path, call the next filter in the chain
         * and terminate this filters execution
         * 2 retrieving the access token from the requestHeader
         * 3 Decode access token
         * 4 extract token permission
         * 5 add token permission to the security context
         * 6 call the next filterChain
         */
        String requestPath = request.getServletPath();
        boolean isRequestPathPublic = SecurityUtils.END_POINTS.contains(requestPath);
        if (isRequestPathPublic) {
            filterChain.doFilter(request,response);
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader != null){
        String token = authorizationHeader.substring(JWT_PREFIX.length()).strip();
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512("secret".getBytes()))
                .withIssuer("mavericks_hub")
                .withClaimPresence("roles")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        List<SimpleGrantedAuthority> authorities =  decodedJWT.getClaim("roles").asList(SimpleGrantedAuthority.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);}
        filterChain.doFilter(request,response);
    }
}
