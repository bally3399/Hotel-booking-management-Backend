package topg.bimber_user_service.service;


import topg.bimber_user_service.models.Token;

public interface TokenService {

    String  createToken(String email);

    Token findByUserEmail(String email);

    void deleteToken(String id);

}
