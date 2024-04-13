package com.d111.backend.dto.feed.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FittingRequest {

    @Schema(description = "피드 번호", example = "1")
    private Long feedId;

    @Schema(description = "새로운 피드 제목", example = "새로운 제목")
    private String newFeedTitle;

    @Schema(description = "새로운 피드 내용", example = "새로운 내용")
    private String newFeedContent;

    private String coordiId;

}

