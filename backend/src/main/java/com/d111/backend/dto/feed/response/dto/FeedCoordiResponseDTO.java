package com.d111.backend.dto.feed.response.dto;

import com.d111.backend.dto.recommend.response.ClothResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedCoordiResponseDTO {

    private ClothResponseDTO outerCloth;

    private ClothResponseDTO upperBody;

    private ClothResponseDTO lowerBody;

    private ClothResponseDTO dress;

}
