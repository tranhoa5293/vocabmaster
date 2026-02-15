package com.softfine.vocabmaster.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActiveLearnerResponse {
    private String userId;
    private String name;
    private Instant lastActiveAt;
    private Long currentLessonId;
    private String currentMode;
}
