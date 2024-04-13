package com.d111.backend.entity.user;

import com.d111.backend.entity.closet.Closet;
import com.d111.backend.entity.comment.Comment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_nickname", length = 50, nullable = false)
    private String nickname;

    @Builder.Default
    @Column(name = "user_profile_image")
    private String profileImage = "";

    @Column(name = "user_like_categories")
    private String likeCategories;

    @Column(name = "user_dislike_categories")
    private String dislikeCategories;

    @Column(name = "user_height")
    private Integer height;

    @Column(name = "user_weight")
    private Integer weight;

    @Builder.Default
    @Column(name = "user_introduce")
    private String introduce = "";

    @Builder.Default
    @Column(name = "user_instagram")
    private String instagram = "";

    @Builder.Default
    @Column(name = "user_youtube")
    private String youtube = "";

    @Builder.Default
    @OneToMany(mappedBy = "userId")
    private List<Closet> closets = new ArrayList<>();

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImagePath) {
        this.profileImage = profileImagePath;
    }

    public void updateLikeCategories(String likeCategories) {
        this.likeCategories = likeCategories;
    }

    public void updateDislikeCategories(String dislikeCategories) {
        this.dislikeCategories = dislikeCategories;
    }

    public void updateHeight(int height) {
        this.height = height;
    }

    public void updateWeight(int weight) {
        this.weight = weight;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void updateInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void updateYoutube(String youtube) { this.youtube = youtube; }

    public void addCloset(Closet closet) {
        closets.add(closet);
    }

    public void deleteCloset(Closet closet) {
        closets.remove(closet);
    }

}
