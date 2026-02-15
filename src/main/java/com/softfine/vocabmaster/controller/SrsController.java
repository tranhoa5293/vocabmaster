package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.SrsUpdateRequest;
import com.softfine.vocabmaster.domain.dto.UserVocabularyResponse;
import com.softfine.vocabmaster.domain.dto.SrsBatchUpdateItem;
import com.softfine.vocabmaster.domain.entity.UserVocabulary;
import com.softfine.vocabmaster.service.SrsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/v1/srs")
@AllArgsConstructor
public class SrsController {

    private final SrsService srsService;

    @PostMapping("/update")
    public UserVocabularyResponse updateSrs(@RequestBody SrsUpdateRequest request) {
        UserVocabulary updated = srsService.updateSrs(request.getVocabId(), request.getScore());
        return UserVocabularyResponse.from(updated);
    }

    @PostMapping("/batch-update")
    public List<UserVocabularyResponse> updateSrsBatch(@RequestBody List<SrsBatchUpdateItem> updates) {
        return srsService.updateSrsBatch(updates).stream()
                .map(UserVocabularyResponse::from)
                .toList();
    }
}
