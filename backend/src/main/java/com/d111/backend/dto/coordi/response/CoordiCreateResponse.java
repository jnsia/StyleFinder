package com.d111.backend.dto.coordi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoordiCreateResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private Boolean data;

    public static CoordiCreateResponse createCoordiCreateResponse(String message, Boolean isCreated){
        return CoordiCreateResponse.builder()
                .message(message)
                .data(isCreated)
                .build();
    }
}

