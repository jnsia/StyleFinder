package com.d111.backend.entity.comment;

import com.d111.backend.entity.feed.Feed;
import com.d111.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String content;

    @Column(name = "comment_created_date")
    private LocalDate createdDate;

    @Column(name = "comment_updated_date")
    private LocalDate updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feedId;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateUpdatedDate(LocalDate updatedDate) { this.updatedDate = updatedDate; }

}
