package com.d111.backend.dto.user.response;

import com.d111.backend.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class GetUserResponseDTO {

    @Schema(description = "닉네임")
    String nickname;

    @Schema(description = "선호하는 옷 종류", example = "원피스,청바지,니트")
    List<String> likeCategories;

    @Schema(description = "불호하는 옷 종류", example = "후드티,티셔츠")
    List<String> dislikeCategories;

    @Schema(description = "키")
    Integer height;

    @Schema(description = "몸무게")
    Integer weight;

    @Schema(description = "프로필 이미지", example = "binary")
    private byte[] profileImage;

    @Schema(description = "유저 소개", example = "안녕")
    private String introduce = "";

    @Schema(description = "유저 인스타그램", example = "instagram.com")
    private String instagram = "";

    @Schema(description = "유저 유튜브", example = "youtube.com")
    private String youtube = "";



    public static GetUserResponseDTO creategetUserResponseDTO(Optional<User> user){


        return GetUserResponseDTO.builder()
                .nickname(user.get().getNickname())
                .likeCategories(Collections.singletonList(user.get().getLikeCategories()))
                .dislikeCategories(Collections.singletonList(user.get().getDislikeCategories()))
                .height(user.get().getHeight())
                .weight(user.get().getWeight())
                .profileImage(user.get().getProfileImage().getBytes())
                .introduce(user.get().getIntroduce())
                .instagram(user.get().getInstagram())
                .youtube(user.get().getYoutube())
                .build();
    }


}
