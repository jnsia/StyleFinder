package com.d111.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInRequestDTO {

    @Schema(name = "email", example = "example@gmail.com")
    String email;

    @Schema(name = "password")
    String password;

}