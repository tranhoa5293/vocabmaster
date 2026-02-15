package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.FavoriteLesson;
import com.softfine.vocabmaster.domain.entity.FavoriteLessonId;
import com.softfine.vocabmaster.domain.entity.Lesson;
import com.softfine.vocabmaster.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteLessonRepository extends JpaRepository<FavoriteLesson, FavoriteLessonId> {
    boolean existsByUserAndLesson(User user, Lesson lesson);
    void deleteByUserAndLesson(User user, Lesson lesson);

    @Query("select fl.lesson.id from FavoriteLesson fl where fl.user = :user")
    List<Long> findFavoriteLessonIds(User user);
}
