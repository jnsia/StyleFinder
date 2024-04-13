package com.d111.backend.repository.comment;

import com.d111.backend.entity.comment.Comment;
import com.d111.backend.entity.feed.Feed;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByFeedId(Feed feedId);

}
