package topg.bimber_user_service.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    // Secret key used for signing the JWT (ensure it's long enough for HS256)
    private static final String SECRET_KEY = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";  // Long enough for HS256
    private final long EXPIRATION_TIME = 864_000_000;
    private final long REFRESH_EXPIRATION_TIME = 604800000;  // 7 days in milliseconds

    // Extract username from token
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    // Extract claim (like expiration) from token
    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey(SECRET_KEY))  // Use getKey() to handle secret key properly
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get the SecretKey from the provided key string (base64 decoding)
    private SecretKey getKey(String secret) {
        byte[] decodedKey = Decoders.BASE64.decode(secret);  // Base64 decode the secret key
        return Keys.hmacShaKeyFor(decodedKey);  // Create the SecretKey for signing
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    // Generate JWT Token with specified expiration time
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails, EXPIRATION_TIME);
    }

    // Generate JWT Token with custom claims and expiration time
    private String generateToken(Map<String, Object> claims, UserDetails userDetails, long expirationTime) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("role", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate a refresh token (7 days expiration)
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, REFRESH_EXPIRATION_TIME);
    }

    // Validate the token (check if username matches and token is not expired)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
