package topg.bimber_user_service.dto.requests;

public record LoginRequestDto(
        String username,
        String password
) {
}
