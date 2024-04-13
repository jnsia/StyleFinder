package com.d111.backend.entity.closet;

import com.d111.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Closet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closet_id")
    private Long id;

    @Column(name = "closet_image")
    private String image;

    @Column(name = "closet_category")
    private String categories;

    @Column(name = "closet_details")
    private String details;

    @Column(name = "closet_textures")
    private String textures;

    @Enumerated(EnumType.STRING)
    @Column(name = "closet_part")
    private Part part;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

}
