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
public class UserVocabularyProgressItem {
    private String userId;
    private String vocabId;
    private String word;
    private double easeFactor;
    private int interval;
    private int repetition;
    private Instant nextReviewAt;
    private String lastResult;
    private int totalCorrect;
    private int totalWrong;
}
