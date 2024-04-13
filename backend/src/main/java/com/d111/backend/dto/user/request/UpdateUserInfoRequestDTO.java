package com.d111.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserInfoRequestDTO {

    @Schema(description = "닉네임")
    String nickname;

    @Schema(description = "선호하는 옷 종류", nullable = true, example = "[원피스, 청바지, 니트]")
    List<String> likeCategories;

    @Schema(description = "불호하는 옷 종류", nullable = true, example = "[후드티, 티셔츠]")
    List<String> dislikeCategories;

    @Schema(description = "키", nullable = true)
    Integer height;

    @Schema(description = "몸무게", nullable = true)
    Integer weight;

    @Schema(description = "한 줄 소개")
    String introduce;

    @Schema(description = "인스타그램 주소")
    String instagram;

    @Schema(description = "유튜브 주소")
    String youtube;

}
