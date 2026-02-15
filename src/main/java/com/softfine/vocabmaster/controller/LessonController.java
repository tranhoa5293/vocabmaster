package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.LessonResponse;
import com.softfine.vocabmaster.domain.dto.SaveLessonRequest;
import com.softfine.vocabmaster.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collections")
@AllArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/{collectionId}/lessons")
    public List<LessonResponse> getLessons(@PathVariable Long collectionId) {
        return lessonService.getLessons(collectionId);
    }

    @PostMapping("/{collectionId}/lessons")
    public LessonResponse saveLesson(@PathVariable Long collectionId, @RequestBody SaveLessonRequest request) {
        return lessonService.saveLesson(collectionId, request.getName());
    }
}
