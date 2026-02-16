package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "collections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column
    private String category;

    @Column
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "favorite_count", columnDefinition = "bigint default 0")
    private long favoriteCount = 0;

    @Column(name = "enroll_count", columnDefinition = "bigint default 0")
    private long enrollCount = 0;
}
