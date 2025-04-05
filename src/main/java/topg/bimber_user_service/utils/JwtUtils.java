package topg.bimber_user_service.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;



public class JwtUtils {


    public static String generateAccessToken(String id){
        return JWT.create()
                .withClaim("user_id", id)
                .withIssuer("admin_db")
                .sign(Algorithm.HMAC256("secret"));
    }

}
