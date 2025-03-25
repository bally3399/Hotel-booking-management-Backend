package topg.bimber_user_service.exceptions;

public class UnauthorizedException extends HotelBookingManagementException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
