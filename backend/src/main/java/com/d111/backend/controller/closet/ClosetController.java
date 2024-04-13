package com.d111.backend.controller.closet;

import com.d111.backend.dto.closet.response.ClosetListReadResponseDTO;
import com.d111.backend.dto.closet.response.ClosetUploadResponseDTO;
import com.d111.backend.dto.recommend.response.ClothResponseDTO;
import com.d111.backend.dto.sample.response.SampleTestResponseDTO;
import com.d111.backend.service.closet.ClosetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/closet")
@RequiredArgsConstructor
public class ClosetController {

    private final ClosetService closetService;

    @Operation(summary = "내 옷 저장", description = "내 옷장에 옷과 옷에 대한 속성을 저장합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "저장 성공", content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping(value = "/uploadCloset", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ClosetUploadResponseDTO> uploadCloth(@RequestParam String clothPart, @RequestPart MultipartFile clothImage) throws Exception {
        return closetService.uploadCloset(clothPart, clothImage);
    }

    @Operation(summary = "내 옷 조회", description = "내 옷장에 있는 옷들을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = String.class))),
    })
    @GetMapping("/getAll")
    ResponseEntity<List<ClosetListReadResponseDTO>> getClosets(@RequestParam String part) {
        return closetService.getClosets(part);
    }

    @Operation(summary = "내 옷 제거", description = "내 옷장에서 옷을 제거합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "203", description = "삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
    })
    @DeleteMapping("/delete/{closetId}")
    ResponseEntity<String> deleteCloset(@PathVariable Long closetId) {
        return closetService.deleteCloset(closetId);
    }

    @GetMapping("/getCloth/{closetId}")
    ResponseEntity<ClothResponseDTO> getCloset(@PathVariable Long closetId) {
        return closetService.getCloset(closetId);
    }

}
