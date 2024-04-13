package com.d111.backend.controller.mongo;

import com.d111.backend.dto.coordi.request.CoordiCreateRequest;
import com.d111.backend.dto.coordi.response.CoordiCreateResponse;
import com.d111.backend.service.coordi.MongoCoordiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Coordi", description = "Coordi API")
@RequestMapping("/api/coordi")
@RestController
@RequiredArgsConstructor
public class MongoCoordiController {

    private final MongoCoordiService mongoCoordiService;

    @Operation(summary = "coordi 생성", description = "새로운 coordi를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = CoordiCreateRequest.class)))
    })
    @PostMapping
    public ResponseEntity<CoordiCreateResponse> coordiCreate(@RequestBody CoordiCreateRequest coordiCreateRequest) {
        return mongoCoordiService.create(coordiCreateRequest);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CoordiReadResponse> coordiRead(@PathVariable String id) {
//        return mongoCoordiService.read(id);
//    }
}
