package com.d111.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenReissueRequestDTO {

    @Schema(description = "리프레시 토큰")
    String refreshToken;

}
