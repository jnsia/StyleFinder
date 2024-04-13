package com.d111.backend.dto.feed.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedDeleteResponse {

    @Schema(description = "상태 메시지", example = "Success")
    private String message;

    @Schema(description = "데이터")
    private Boolean data;

    public static FeedDeleteResponse createFeedDeleteResponse(String message, boolean isDeleted) {
        return FeedDeleteResponse.builder()
                .message(message)
                .data(isDeleted)
                .build();
    }
}
