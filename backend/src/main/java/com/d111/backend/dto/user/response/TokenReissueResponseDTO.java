package com.d111.backend.dto.user.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenReissueResponseDTO {

    @Schema(description = "액세스 토큰")
    String accessToken;

}
