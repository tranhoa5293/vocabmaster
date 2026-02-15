package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_vocabulary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVocabulary {

    @EmbeddedId
    private UserVocabularyId id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @MapsId("vocabId")
    @JoinColumn(name = "vocab_id")
    private Vocabulary vocabulary;

    @Column(name = "word")
    private String word;

    @Column(name = "ease_factor", nullable = false)
    private double easeFactor = 2.5;

    @Column(nullable = false)
    private int interval = 0;

    @Column(nullable = false)
    private int repetition = 0;

    @Column(name = "next_review_at")
    private Instant nextReviewAt;

    @Column(name = "last_result")
    private String lastResult;

    @Column(name = "total_correct", nullable = false)
    private int totalCorrect = 0;

    @Column(name = "total_wrong", nullable = false)
    private int totalWrong = 0;

    public UserVocabulary(User user, Vocabulary vocabulary) {
        this.user = user;
        this.vocabulary = vocabulary;
        this.word = vocabulary.getWord();
        this.id = new UserVocabularyId(user.getId(), vocabulary.getId());
    }
}
