package com.d111.backend.entity.coordi;

import com.d111.backend.dto.coordi.request.CoordiCreateRequest;
import com.d111.backend.entity.feed.Feed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Coordi")
public class Coordi {

    @Id
    private String _id;

    @Embedded
    @Column(name = "outer_cloth")
    private ClothInfo outerCloth;

    @Embedded
    @Column(name = "upper_body")
    private ClothInfo upperBody;

    @Embedded
    @Column(name = "lower_body")
    private ClothInfo lowerBody;

    @Embedded
    @Column(name = "dress")
    private ClothInfo dress;

    @Embedded
    @Column(name = "image_url")
    private ClothInfo imageUrl;

    @OneToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public static Coordi createCoordi(CoordiCreateRequest coordiCreateRequest) {
        Coordi coordi = new Coordi();

        coordi.setOuterCloth(coordiCreateRequest.getOuterCloth() != null ?
                createClothInfo(coordiCreateRequest.getOuterCloth()) : null);
        coordi.setUpperBody(coordiCreateRequest.getUpperBody() != null ?
                createClothInfo(coordiCreateRequest.getUpperBody()) : null);
        coordi.setLowerBody(coordiCreateRequest.getLowerBody() != null ?
                createClothInfo(coordiCreateRequest.getLowerBody()) : null);
        coordi.setDress(coordiCreateRequest.getDress() != null ?
                createClothInfo(coordiCreateRequest.getDress()) : null);

        return coordi;
    }

    private static ClothInfo createClothInfo(ClothInfo clothInfoDto) {
        return ClothInfo.builder()
                .style(clothInfoDto.getStyle())
                .category(clothInfoDto.getCategory())
                .color(clothInfoDto.getColor())
                .build();
    }
}
