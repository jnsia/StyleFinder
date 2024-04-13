package com.d111.backend.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AnalysisFavorResponseDTO {

    @Schema(description = "선호 카테고리")
    Map<String, Integer> likeCategories;

    @Schema(description = "내 옷장에 있는 패션 아이템에 대한 카테고리")
    Map<String, Integer> closetCategories;

    @Schema(description = "내 피드에 있는 패션 아이템에 대한 카테고리")
    Map<String, Integer> feedStyles;

    @Schema(description = "내 피드에 있는 패션 아이템에 대한 카테고리")
    Map<String, Integer> feedCategories;

}
