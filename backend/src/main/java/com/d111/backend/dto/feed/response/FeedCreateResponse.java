package com.d111.backend.dto.feed.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedCreateResponse {

    @Schema(description = "상태 메시지", example = "Success")
    private String message;

    @Schema(description = "데이터")
    private Boolean data;

    public static FeedCreateResponse createFeedCreateResponse(String message, boolean isCreated) {
        return FeedCreateResponse.builder()
                .message(message)
                .data(isCreated)
                .build();
    }
}
