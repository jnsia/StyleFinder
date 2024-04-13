package com.d111.backend.dto.feed.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedUpdateRequest {

    @Schema(description = "피드 제목", example = "제목입니다")
    String feedTitle;

    @Schema(description = "피드 내용", example = "내용입니다.")
    String feedContent;

}
