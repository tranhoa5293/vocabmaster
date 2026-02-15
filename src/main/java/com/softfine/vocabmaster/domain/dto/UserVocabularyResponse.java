package com.softfine.vocabmaster.domain.dto;

import com.softfine.vocabmaster.domain.entity.UserVocabulary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserVocabularyResponse {
    private String userId;
    private String vocabId;
    private String word;
    private double easeFactor;
    private int interval;
    private int repetition;
    private String nextReviewAt;
    private String lastResult;
    private int totalCorrect;
    private int totalWrong;

    public static UserVocabularyResponse from(UserVocabulary uv) {
        return new UserVocabularyResponse(
                uv.getUser().getId().toString(),
                uv.getVocabulary().getId().toString(),
                uv.getVocabulary().getWord(),
                uv.getEaseFactor(),
                uv.getInterval(),
                uv.getRepetition(),
                uv.getNextReviewAt() == null ? null : uv.getNextReviewAt().toString(),
                uv.getLastResult(),
                uv.getTotalCorrect(),
                uv.getTotalWrong()
        );
    }
}
