package com.d111.backend.dto.feed.response.dto;

import com.d111.backend.dto.coordi.response.dto.CoordiContainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedReadResponseDTO {

    private Long id;

    @Schema(description = "피드 작성자")
    private FeedUserDTO user;

    @Schema(description = "최초 등록자", example = "킹치욱")
    private Long originWriter;

    @Schema(description = "피드 제목", example = "멋있는 코디")
    private String feedTitle;

    @Schema(description = "아우터 이미지", example = "example.com")
    private byte[] outerCloth;

    @Schema(description = "드레스 이미지", example = "example.com")
    private byte[] dress;

    @Schema(description = "상의 이미지", example = "example.com")
    private byte[] upperBody;

    @Schema(description = "하의 이미지", example = "example.com")
    private byte[] lowerBody;

    @Schema(description = "피드 내용", example = "내용입니다")
    private String feedContent;

    @Schema(description = "피드 생성일")
    private LocalDate feedCreatedDate;

    @Schema(description = "피드 수정일")
    private LocalDate feedUpdatedDate;

    @Schema(description = "피드 좋아요")
    private Long feedLikes;

    private CoordiContainer coordiContainer;

    @Schema(description = "댓글 목록")
    private List<FeedCommentDTO> comments;

}
