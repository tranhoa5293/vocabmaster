package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.ToggleFavoriteResponse;
import com.softfine.vocabmaster.service.CollectionService;
import com.softfine.vocabmaster.service.LessonService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FavoriteController {

    private final CollectionService collectionService;
    private final LessonService lessonService;

    public FavoriteController(CollectionService collectionService, LessonService lessonService) {
        this.collectionService = collectionService;
        this.lessonService = lessonService;
    }

    @PostMapping("/collections/{collectionId}/favorite")
    public ToggleFavoriteResponse toggleFavoriteCollection(@PathVariable Long collectionId) {
        boolean isFavorite = collectionService.toggleFavorite(collectionId);
        return new ToggleFavoriteResponse(isFavorite);
    }

    @PostMapping("/lessons/{lessonId}/favorite")
    public ToggleFavoriteResponse toggleFavoriteLesson(@PathVariable Long lessonId) {
        boolean isFavorite = lessonService.toggleFavorite(lessonId);
        return new ToggleFavoriteResponse(isFavorite);
    }
}
