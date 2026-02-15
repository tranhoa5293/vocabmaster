package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.Lesson;
import com.softfine.vocabmaster.domain.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findByLesson(Lesson lesson);
    List<Vocabulary> findByLessonId(Long lessonId);
}
