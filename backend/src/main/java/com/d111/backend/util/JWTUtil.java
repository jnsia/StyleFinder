package com.d111.backend.util;

import com.d111.backend.entity.user.User;
import com.d111.backend.exception.user.CustomJWTException;
import com.d111.backend.exception.user.EmailNotFoundException;
import com.d111.backend.repository.user.UserRepository;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class JWTUtil {

    private final UserRepository userRepository;

    static SecretKey key = Jwts.SIG.HS256.key().build();

    public static String createToken(String email, int minute){
        return Jwts.builder()
                .signWith(key)
                .issuer(email)
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(minute).toInstant()))
                .subject("USER")
                .compact();
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try{
            claim = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token) // 파싱 및 검증, 실패 시 에러
                    .getPayload();
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJWTException("MALFORMED_TOKEN");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException("TOKEN_EXPIRED");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("INVALID_TOKEN");
        } catch (JwtException jwtException) {
            throw new CustomJWTException("JWT_TOKEN_ERROR");
        }

        return claim;
    }

    public static String findEmailByToken(){
        // 현재 접속한 유저 정보를 가져오는 메서드
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication);
        return authentication.getName();
    }
}
