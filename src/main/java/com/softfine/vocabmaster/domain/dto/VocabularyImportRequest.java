package com.softfine.vocabmaster.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyImportRequest {
    private String word;
    private String meaning;
    private String pronunciation;
    private String example;
}
