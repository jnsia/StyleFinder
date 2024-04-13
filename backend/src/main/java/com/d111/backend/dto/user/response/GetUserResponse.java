package com.d111.backend.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private GetUserResponseDTO data;

    public static GetUserResponse creategetUserResponse(String message, GetUserResponseDTO dto) {
        return GetUserResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
