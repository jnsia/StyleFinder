package com.d111.backend.dto.feed.response.dto;

import com.d111.backend.dto.feed.response.FeedReadResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedCommentDTO {

    @Schema(description = "댓글 작성자 닉네임")
    private String nickname;

    @Schema(description = "댓글 작성자 프로필 이미지")
    private byte[] profileImage;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "작성일")
    private LocalDate commentCreatedDate;

    @Schema(description = "수정일")
    private LocalDate commentUpdatedDate;

}
