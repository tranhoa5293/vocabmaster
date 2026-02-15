package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.ProgressPageResponse;
import com.softfine.vocabmaster.domain.dto.StudyStatsResponse;
import com.softfine.vocabmaster.domain.dto.VocabularyImportRequest;
import com.softfine.vocabmaster.domain.dto.VocabularyResponse;
import com.softfine.vocabmaster.service.VocabularyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vocabulary")
@AllArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @GetMapping("/lessons/{lessonId}")
    public List<VocabularyResponse> getVocabulary(@PathVariable Long lessonId) {
        return vocabularyService.getVocabulary(lessonId);
    }

    @PostMapping("/lessons/{lessonId}/import")
    public List<VocabularyResponse> importVocabulary(@PathVariable Long lessonId, @RequestBody List<VocabularyImportRequest> items) {
        return vocabularyService.importVocabulary(lessonId, items);
    }

    @GetMapping("/due")
    public List<VocabularyResponse> getDueVocabulary(@RequestParam(defaultValue = "10") int limit,
                                                     @RequestParam(required = false) Long lessonId,
                                                     @RequestParam(required = false) String mode) {
        return vocabularyService.getDueVocabulary(limit, lessonId, mode);
    }

    @GetMapping("/progress")
    public ProgressPageResponse getUserProgress(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return vocabularyService.getUserProgress(page, size);
    }

    @GetMapping("/stats")
    public StudyStatsResponse getStudyStats() {
        return vocabularyService.getStudyStats();
    }
}
