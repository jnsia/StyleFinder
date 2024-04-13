package com.d111.backend.entity.coordi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothInfo {
    private String style;
    private String category;
    private String color;

    private static ClothInfo createClothInfo(ClothInfo clothInfoDto) {
        return ClothInfo.builder()
                .style(clothInfoDto.getStyle())
                .category(clothInfoDto.getCategory())
                .color(clothInfoDto.getColor())
                .build();
    }
}
