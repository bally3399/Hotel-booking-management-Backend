package topg.bimber_user_service.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String provideValidMail) {
        super(provideValidMail);
    }
}
