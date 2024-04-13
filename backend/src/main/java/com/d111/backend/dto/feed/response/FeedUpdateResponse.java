package com.d111.backend.dto.feed.response;

import com.d111.backend.dto.feed.response.dto.FeedUpdateResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedUpdateResponse {


    @Schema(description = "상태 메시지", example = "Success")
    private String message;

    @Schema(description = "데이터")
    private FeedUpdateResponseDTO data;

    public static FeedUpdateResponse createFeedUpdateResponse(String message, FeedUpdateResponseDTO dto) {
        return FeedUpdateResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
