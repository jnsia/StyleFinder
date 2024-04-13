package com.d111.backend.service.recommend;

import com.d111.backend.dto.recommend.request.RecommendListRequestDTO;
import com.d111.backend.dto.recommend.response.ClothResponseDTO;
import com.d111.backend.dto.recommend.response.RecommendListResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RecommendService {

    ResponseEntity<RecommendListResponseDTO> getRecommendItems(RecommendListRequestDTO recommendListRequestDTO);

    ResponseEntity<RecommendListResponseDTO> recommendItems(RecommendListRequestDTO recommendListRequestDTO);

    ResponseEntity<List<ClothResponseDTO>> getStyleRecommend(String style);

    ResponseEntity<List<ClothResponseDTO>> getCategoryRecommend(String category);

    ResponseEntity<List<ClothResponseDTO>> getColorRecommend(String color);

}
