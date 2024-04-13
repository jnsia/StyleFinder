package com.d111.backend.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserInfoResponseDTO {

    @Schema(description = "닉네임")
    String nickname;

    @Schema(description = "선호하는 옷 종류", example = "원피스,청바지,니트")
    String likeCategories;

    @Schema(description = "불호하는 옷 종류", example = "후드티,티셔츠")
    String dislikeCategories;

    @Schema(description = "키")
    Integer height;

    @Schema(description = "몸무게")
    Integer weight;

    @Schema(description = "프로필 이미지", example = "binary")
    private byte[] profileImage;

}
