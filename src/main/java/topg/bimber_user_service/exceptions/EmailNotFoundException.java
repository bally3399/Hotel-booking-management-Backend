package topg.bimber_user_service.exceptions;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String emailNotFound) {
        super(emailNotFound);
    }
}
