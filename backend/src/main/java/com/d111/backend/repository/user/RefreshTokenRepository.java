package com.d111.backend.repository.user;

import com.d111.backend.entity.user.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAccessToken(String accessToken);

}
