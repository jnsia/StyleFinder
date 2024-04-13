package com.d111.backend.dto.feed.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedListUserDTO {

    @Schema(description = "닉네임")
    String nickname;

    @Schema(description = "선호하는 옷 종류", example = "원피스,청바지,니트")
    List<String> likeCategories;

    @Schema(description = "불호하는 옷 종류", example = "후드티,티셔츠")
    List<String> dislikeCategories;

    @Schema(description = "프로필 이미지", example = "binary")
    byte[] profileImage;

    @Schema(description = "유저 소개", example = "안녕")
    String introduce;

    @Schema(description = "유저 인스타그램", example = "instagram.com")
    String instagram;

    @Schema(description = "유저 유튜브", example = "youtube.com")
    String youtube;

}
