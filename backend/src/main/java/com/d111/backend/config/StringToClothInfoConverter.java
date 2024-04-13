package com.d111.backend.config;

import com.d111.backend.entity.coordi.ClothInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToClothInfoConverter implements Converter<String, ClothInfo> {

    @Override
    public ClothInfo convert(String source) {
        String[] parts = source.split(","); // 예시로 쉼표를 기준으로 문자열을 분할합니다.
        ClothInfo clothInfo = new ClothInfo();
        clothInfo.setStyle(parts[0]); // 첫 번째 부분을 스타일로 설정합니다.
        clothInfo.setCategory(parts[1]); // 두 번째 부분을 카테고리로 설정합니다.
        clothInfo.setColor(parts[2]); // 세 번째 부분을 색상으로 설정합니다.

        return clothInfo;
    }
}
