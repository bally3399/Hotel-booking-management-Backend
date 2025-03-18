package topg.bimber_user_service.dto.responses;

public record JwtResponseDto(

        boolean success,
        String token
) {
}