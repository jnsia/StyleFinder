package com.d111.backend.dto.feed.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedUserDTO {

    @Schema(description = "닉네임")
    String nickname;

    @Schema(description = "프로필 이미지", example = "binary")
    byte[] profileImage;

    Boolean isLiked;

}
