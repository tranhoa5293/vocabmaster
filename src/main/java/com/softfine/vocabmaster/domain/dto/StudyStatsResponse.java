package com.softfine.vocabmaster.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyStatsResponse {
    private long totalLearning;
    private long dueCount;
    private long masteredCount;
}
