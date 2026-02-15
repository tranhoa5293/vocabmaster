package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_enroll")
public class UserEnroll {

    @EmbeddedId
    private UserEnrollId id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected UserEnroll() {
    }

    public UserEnroll(User user, Lesson lesson, Instant createdAt) {
        this.user = user;
        this.lesson = lesson;
        this.createdAt = createdAt;
        this.id = new UserEnrollId(user.getId(), lesson.getId());
    }

    public UserEnrollId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
