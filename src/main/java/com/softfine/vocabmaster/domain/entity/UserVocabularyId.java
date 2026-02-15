package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class UserVocabularyId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "vocab_id")
    private Long vocabId;

    protected UserVocabularyId() {
    }

    public UserVocabularyId(Long userId, Long vocabId) {
        this.userId = userId;
        this.vocabId = vocabId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVocabularyId that = (UserVocabularyId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocabId, that.vocabId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, vocabId);
    }
}
