package com.d111.backend.dto.sample.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleTestResponseDTO {

    @Schema(description = "sample 이름", nullable = false, example = "적당한 무언가")
    String sampleColumn;

}
