package com.softfine.vocabmaster.domain.dto;

import com.softfine.vocabmaster.domain.entity.Vocabulary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyResponse {
    private String id;
    private String lessonId;
    private String word;
    private String meaning;
    private String pronunciation;
    private String example;

    public static VocabularyResponse from(Vocabulary vocabulary) {
        return new VocabularyResponse(
                vocabulary.getId().toString(),
                vocabulary.getLesson().getId().toString(),
                vocabulary.getWord(),
                vocabulary.getMeaning(),
                vocabulary.getPronunciation(),
                vocabulary.getExample()
        );
    }
}
