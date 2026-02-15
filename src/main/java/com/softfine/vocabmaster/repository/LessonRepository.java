package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.Collection;
import com.softfine.vocabmaster.domain.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCollection(Collection collection);
    Optional<Lesson> findByIdAndCollection(Long id, Collection collection);
}
