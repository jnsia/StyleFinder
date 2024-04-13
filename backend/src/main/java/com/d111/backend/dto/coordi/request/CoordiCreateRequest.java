package com.d111.backend.dto.coordi.request;

import com.d111.backend.entity.coordi.ClothInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordiCreateRequest {

    private ClothInfo outerCloth;

    private ClothInfo upperBody;

    private ClothInfo lowerBody;

    private ClothInfo dress;
}
