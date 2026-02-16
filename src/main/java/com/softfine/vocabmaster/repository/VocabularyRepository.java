package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.Lesson;
import com.softfine.vocabmaster.domain.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findByLesson(Lesson lesson);
    List<Vocabulary> findByLessonId(Long lessonId);
    long countByLessonId(Long lessonId);
    @Query(value = "select v.* from vocabulary v LEFT JOIN user_vocabulary uv " +
            "  ON uv.vocab_id = v.id AND uv.user_id = :userId " +
            "WHERE v.lesson_id = :lessonId and uv.vocab_id IS NULL " +
            "ORDER BY random() " +
            "LIMIT :limit", nativeQuery = true)
    List<Vocabulary> findNewWord(Long userId, Long lessonId, long limit);

    @Query(value = "select v.* from vocabulary v LEFT JOIN user_vocabulary uv " +
            "  ON uv.vocab_id = v.id AND uv.user_id = :userId " +
            "WHERE uv.vocab_id IS NULL " +
            "ORDER BY random() " +
            "LIMIT :limit", nativeQuery = true)
    List<Vocabulary> findNewWord(Long userId, long limit);
}
