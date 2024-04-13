package com.d111.backend.controller.recommend;

import com.d111.backend.dto.recommend.request.RecommendListRequestDTO;
import com.d111.backend.dto.recommend.response.ClothResponseDTO;
import com.d111.backend.dto.recommend.response.RecommendListResponseDTO;
import com.d111.backend.service.recommend.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/recommend")
@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping
    public ResponseEntity<RecommendListResponseDTO> recommend(@RequestBody RecommendListRequestDTO recommendListRequestDTO) {
        return recommendService.getRecommendItems(recommendListRequestDTO);
    }

    @GetMapping("/style")
    public ResponseEntity<List<ClothResponseDTO>> styleRecommend(@RequestParam String style) {
        return recommendService.getStyleRecommend(style);
    }

    @GetMapping("/category")
    public ResponseEntity<List<ClothResponseDTO>> categoryRecommend(@RequestParam String category) {
        return recommendService.getCategoryRecommend(category);
    }

    @GetMapping("/color")
    public ResponseEntity<List<ClothResponseDTO>> colorRecommend(@RequestParam String color) {
        return recommendService.getColorRecommend(color);
    }

}
