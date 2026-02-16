package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vocabulary")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String meaning;

    @Column(nullable = false)
    private String pronunciation;

    @Column
    private String example;
}
