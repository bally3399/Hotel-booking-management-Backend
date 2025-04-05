package topg.bimber_user_service.security.utils;

import java.util.List;

public class SecurityUtils {
    private SecurityUtils(){}
    public static final List<String > END_POINTS = List.of("/api/v1/auth");
    public static final String JWT_PREFIX ="Bearer ";
}
