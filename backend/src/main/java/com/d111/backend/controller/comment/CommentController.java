package com.d111.backend.controller.comment;

import com.d111.backend.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "피드에 대한 새로운 댓글을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/{feedId}/create")
    ResponseEntity<String> createComment(@PathVariable Long feedId, @RequestParam String content) {
        return commentService.createComment(feedId, content);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{commentId}/update")
    ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestParam String content) {
        return commentService.updateComment(commentId, content);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "203", description = "수정 삭제",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{commentId}/delete")
    ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }
}
