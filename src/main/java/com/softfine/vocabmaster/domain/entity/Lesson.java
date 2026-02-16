package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @Column(nullable = false)
    private String name;

    @Column(name = "favorite_count", columnDefinition = "bigint default 0")
    private long favoriteCount = 0;

    @Column(name = "enroll_count", columnDefinition = "bigint default 0")
    private long enrollCount = 0;

    @Column(name = "total_word", columnDefinition = "bigint default 0")
    private long totalWord = 0;
}
