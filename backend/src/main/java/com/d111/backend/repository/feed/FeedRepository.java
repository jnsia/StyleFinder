package com.d111.backend.repository.feed;

import com.d111.backend.entity.feed.Feed;
import com.d111.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findAllByOrderByFeedLikesDesc(Pageable pageable);

    Page<Feed> findAllByOrderByIdDesc(Pageable pageable);

    Page<Feed> findByfeedTitleContaining(String title, Pageable pageable);

    Page<Feed> findAllByuserId(Optional<User> userId, Pageable pageable);

    List<Feed> findAllByuserId(Optional<User> userId);

    List<Feed> findCoordiIdsByUserId(User userId);
}