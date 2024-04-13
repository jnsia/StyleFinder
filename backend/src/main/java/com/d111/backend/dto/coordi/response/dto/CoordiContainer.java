    package com.d111.backend.dto.coordi.response.dto;

    import com.d111.backend.entity.coordi.ClothInfo;
    import com.d111.backend.entity.coordi.Coordi;
    import com.d111.backend.repository.mongo.MongoCoordiRepository;
    import io.swagger.v3.oas.annotations.media.Schema;
    import jakarta.persistence.Id;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.Optional;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class CoordiContainer {

        @Id
        private String id;

        @Schema(description = "아우터")
        private ClothInfo outerCloth;

        @Schema(description = "상의")
        private ClothInfo upperBody;

        @Schema(description = "하의")
        private ClothInfo lowerBody;

        @Schema(description = "원피스")
        private ClothInfo dress;



        public static CoordiContainer createMongoContainer(String coordiId, MongoCoordiRepository mongoCoordiRepository) {

            Optional<Coordi> coordiOptional = mongoCoordiRepository.findById(coordiId);
            Coordi coordi = coordiOptional.orElseThrow(() -> new RuntimeException("Coordi not found"));

            return CoordiContainer.builder()
                    .id(coordi.get_id())
                    .outerCloth(coordi.getOuterCloth())
                    .upperBody(coordi.getUpperBody())
                    .lowerBody(coordi.getLowerBody())
                    .dress(coordi.getDress())
                    .build();
        }
    }
