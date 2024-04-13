package com.d111.backend.serviceImpl.comment;

import com.d111.backend.entity.comment.Comment;
import com.d111.backend.entity.feed.Feed;
import com.d111.backend.entity.user.User;
import com.d111.backend.exception.comment.CommentNotFoundException;
import com.d111.backend.exception.feed.FeedNotFoundException;
import com.d111.backend.exception.user.EmailNotFoundException;
import com.d111.backend.exception.user.UnauthorizedAccessException;
import com.d111.backend.repository.comment.CommentRepository;
import com.d111.backend.repository.feed.FeedRepository;
import com.d111.backend.repository.user.UserRepository;
import com.d111.backend.service.comment.CommentService;
import com.d111.backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ResponseEntity<String> createComment(Long feedId, String content) {
        String email = JWTUtil.findEmailByToken();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("토큰에 포함된 이메일이 정확하지 않습니다."));

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException("해당하는 피드가 없습니다."));

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));

        Comment comment = Comment.builder()
                .content(content)
                .userId(user)
                .feedId(feed)
                .createdDate(now)
                .updatedDate(now)
                .build();

        feed.addComment(comment);

        feedRepository.save(feed);
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 생성되었습니다.");
    }

    @Override
    public ResponseEntity<String> updateComment(Long commentId, String content) {
        String email = JWTUtil.findEmailByToken();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("토큰에 포함된 이메일이 정확하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new CommentNotFoundException("댓글을 찾지 못했습니다."));

        if (!user.getId().equals(comment.getUserId().getId())) {
            throw new UnauthorizedAccessException("댓글 작성자만 수정할 수 있습니다.");
        }

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));

        comment.updateContent(content);
        comment.updateUpdatedDate(now);

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정이 완료되었습니다.");
    }

    @Override
    public ResponseEntity<String> deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾지 못했습니다."));

        Feed feed = feedRepository.findById(comment.getFeedId().getId())
                .orElseThrow(() -> new FeedNotFoundException("해당 댓글이 포함된 피드가 없습니다."));

        feed.deleteComment(comment);

        feedRepository.save(feed);
        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("댓글 삭제가 완료되었습니다.");
    }

}
