package com.softfine.vocabmaster.domain.dto;

import com.softfine.vocabmaster.domain.entity.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponse {
    private String id;
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private long favoriteCount;
    @JsonProperty("isFavorite")
    private boolean favorite;
    @JsonProperty("activeLearners")
    private long enrollCount;

    public static CollectionResponse from(Collection collection, boolean favorite) {
        return new CollectionResponse(
                collection.getId().toString(),
                collection.getName(),
                collection.getDescription(),
                collection.getCategory(),
                collection.getImageUrl(),
                collection.getFavoriteCount(),
                favorite,
                collection.getEnrollCount()
        );
    }
}
