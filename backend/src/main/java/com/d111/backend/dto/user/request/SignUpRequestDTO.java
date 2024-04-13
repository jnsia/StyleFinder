package com.d111.backend.dto.user.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpRequestDTO {

    @Schema(description = "이메일", nullable = false, example = "example@gmail.com")
    String email;

    @Schema(description = "비밀번호", nullable = false)
    String password;

    @Schema(description = "비밀번호 확인", nullable = false)
    String confirmPassword;

    @Schema(description = "닉네임", nullable = false)
    String nickname;

    @Schema(description = "선호하는 옷 종류", example = "[원피스, 청바지, 니트]")
    List<String> likeCategories;

    @Schema(description = "불호하는 옷 종류", example = "[후드티, 티셔츠]")
    List<String> dislikeCategories;

    @Schema(description = "키")
    Integer height;

    @Schema(description = "몸무게")
    Integer weight;

}
