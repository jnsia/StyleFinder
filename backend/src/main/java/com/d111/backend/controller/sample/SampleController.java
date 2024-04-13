package com.d111.backend.controller.sample;

import com.d111.backend.dto.sample.response.SampleTestResponseDTO;
import com.d111.backend.service.sample.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sample", description = "Sample API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService sampleService;

    @Operation(summary = "모든 sample 조회", description = "모든 sample의 sample_column 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = SampleTestResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "조회 실패", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/getAll")
    ResponseEntity<List<SampleTestResponseDTO>> getSamples() {
        return sampleService.getSamples();
    }

    @Operation(summary = "sample 생성", description = "새로운 sample을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/create")
    ResponseEntity<String> createSample(@RequestParam String sampleName) {
        return sampleService.createSample(sampleName);
    }

}

// 브랜치 동작 확인