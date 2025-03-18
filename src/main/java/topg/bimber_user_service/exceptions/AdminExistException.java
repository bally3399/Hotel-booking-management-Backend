package topg.bimber_user_service.exceptions;

public class AdminExistException extends RuntimeException {
    public AdminExistException(String message) {
        super(message);
    }
}
