package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_score")
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private int date;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected UserScore() {
    }

    public UserScore(User user, int score, String word, int date, Instant createdAt) {
        this.user = user;
        this.score = score;
        this.word = word;
        this.date = date;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public String getWord() {
        return word;
    }

    public int getDate() {
        return date;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
