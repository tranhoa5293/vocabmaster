package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_current_activity")
public class UserCurrentActivity {

    @Id
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    @Column(name = "current_lesson_id")
    private Long currentLessonId;

    @Column(name = "current_mode")
    private String currentMode;

    protected UserCurrentActivity() {
    }

    public UserCurrentActivity(Long userId, String userName, Instant lastActiveAt, Long currentLessonId, String currentMode) {
        this.userId = userId;
        this.userName = userName;
        this.lastActiveAt = lastActiveAt;
        this.currentLessonId = currentLessonId;
        this.currentMode = currentMode;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public Long getCurrentLessonId() {
        return currentLessonId;
    }

    public void setCurrentLessonId(Long currentLessonId) {
        this.currentLessonId = currentLessonId;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }
}
