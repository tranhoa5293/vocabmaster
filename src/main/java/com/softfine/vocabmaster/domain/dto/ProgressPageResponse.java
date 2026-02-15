package com.softfine.vocabmaster.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressPageResponse {
    private List<UserVocabularyProgressItem> items;
    private int totalPages;
    private int currentPages;
}
