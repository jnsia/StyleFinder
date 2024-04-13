package com.d111.backend.repository.Likes;

import com.d111.backend.entity.likes.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByFeedIdAndUserId(Long feedId, Long userId);
}
