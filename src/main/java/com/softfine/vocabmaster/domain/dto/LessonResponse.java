package com.softfine.vocabmaster.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softfine.vocabmaster.domain.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {
    private String id;
    private String collectionId;
    private String name;
    private long favoriteCount;
    @JsonProperty("isFavorite")
    private boolean favorite;
    @JsonProperty("activeLearners")
    private long enrollCount;
    private long totalWord;

    public static LessonResponse from(Lesson lesson, boolean favorite) {
        return new LessonResponse(
                lesson.getId().toString(),
                lesson.getCollection().getId().toString(),
                lesson.getName(),
                lesson.getFavoriteCount(),
                favorite,
                lesson.getEnrollCount(),
                lesson.getTotalWord()
        );
    }
}
