package com.d111.backend.dto.feed.response;

import com.d111.backend.dto.coordi.response.dto.CoordiContainer;
import com.d111.backend.dto.feed.response.dto.FeedReadResponseDTO;
import com.d111.backend.entity.comment.Comment;
import com.d111.backend.entity.feed.Feed;
import com.d111.backend.entity.user.User;
import com.d111.backend.repository.mongo.MongoCoordiRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.d111.backend.dto.coordi.response.dto.CoordiContainer.createMongoContainer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedReadResponse {

    @Schema(description = "상태 메시지", example = "Success")
    private String message;

    @Schema(description = "데이터")
    private FeedReadResponseDTO data;

}