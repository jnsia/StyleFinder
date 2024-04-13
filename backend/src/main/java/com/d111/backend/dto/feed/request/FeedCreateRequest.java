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
public class FeedCreateRequest {

    @Schema(description = "피드 제목", example = "멋진 코디")
    private String feedTitle;

    @Schema(description = "피드 내용", example = "이렇게 입으셈")
    private String feedContent;

    private String outerCloth;

    private String upperBody;

    private String lowerBody;

    private String dress;

    private String coordiId; // Coordi의 _id 추가

}
