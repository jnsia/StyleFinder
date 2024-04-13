package com.d111.backend.service.comment;

import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<String> createComment(Long feedId, String sampleName);

    ResponseEntity<String> updateComment(Long commentId, String content);

    ResponseEntity<String> deleteComment(Long commentId);

}
