package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "favorite_lessons")
public class FavoriteLesson {

    @EmbeddedId
    private FavoriteLessonId id;

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

    protected FavoriteLesson() {
    }

    public FavoriteLesson(User user, Lesson lesson, Instant createdAt) {
        this.user = user;
        this.lesson = lesson;
        this.createdAt = createdAt;
        this.id = new FavoriteLessonId(user.getId(), lesson.getId());
    }

    public FavoriteLessonId getId() {
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
