package com.d111.backend.dto.recommend.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendListResponseDTO {

    private List<ClothResponseDTO> outerCloth;

    private List<ClothResponseDTO> upperBody;

    private List<ClothResponseDTO> lowerBody;

    private List<ClothResponseDTO> dress;

}
