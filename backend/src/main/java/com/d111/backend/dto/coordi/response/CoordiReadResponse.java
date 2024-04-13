package com.d111.backend.dto.coordi.response;

import com.d111.backend.dto.coordi.response.dto.CoordiReadResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoordiReadResponse {

    private String message;

    private CoordiReadResponseDTO data;

    public static CoordiReadResponse createCoordiReadResponse(String message, CoordiReadResponseDTO dto) {
        return CoordiReadResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
