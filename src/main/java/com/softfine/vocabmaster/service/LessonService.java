package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.dto.LessonResponse;
import com.softfine.vocabmaster.domain.entity.Collection;
import com.softfine.vocabmaster.domain.entity.FavoriteLesson;
import com.softfine.vocabmaster.domain.entity.Lesson;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.repository.CollectionRepository;
import com.softfine.vocabmaster.repository.FavoriteLessonRepository;
import com.softfine.vocabmaster.repository.LessonRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.HashSet;

@Service
public class LessonService {

    private final CollectionRepository collectionRepository;
    private final LessonRepository lessonRepository;
    private final FavoriteLessonRepository favoriteLessonRepository;
    private final CurrentUserService currentUserService;
    private final LessonService self;

    public LessonService(CollectionRepository collectionRepository,
                         LessonRepository lessonRepository,
                         FavoriteLessonRepository favoriteLessonRepository,
                         CurrentUserService currentUserService,
                         @Lazy LessonService self) {
        this.collectionRepository = collectionRepository;
        this.lessonRepository = lessonRepository;
        this.favoriteLessonRepository = favoriteLessonRepository;
        this.currentUserService = currentUserService;
        this.self = self;
    }

    public List<LessonResponse> getLessons(Long collectionId) {
        User user = currentUserService.requireCurrentUser();
        var favorites = new HashSet<>(favoriteLessonRepository.findFavoriteLessonIds(user));
        List<LessonResponse> lessonResponses = self.getLessonsCached(collectionId);
        return lessonResponses.stream()
                .peek(response -> response.setFavorite(favorites.contains(Long.valueOf(response.getId()))))
                .sorted((a, b) -> {
                    if (a.isFavorite() != b.isFavorite()) {
                        return a.isFavorite() ? -1 : 1;
                    }
                    return Long.compare(b.getEnrollCount(), a.getEnrollCount());
                })
                .toList();
    }

    @Cacheable(value = "lessons", key = "'all'")
    public List<LessonResponse> getLessonsCached(Long collectionId) {
        return lessonRepository.findByCollection(Collection.builder().id(collectionId).build()).stream()
                .map(lesson -> LessonResponse.from(lesson, false))
                .toList();
    }

    @CacheEvict(value = "lessons", key = "'all'")
    public LessonResponse saveLesson(Long collectionId, String name) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new IllegalArgumentException("Collection not found"));
        Lesson lesson = new Lesson();
        lesson.setCollection(collection);
        lesson.setName(name);
        return LessonResponse.from(lessonRepository.save(lesson), false);
    }

    public boolean toggleFavorite(Long lessonId) {
        User user = currentUserService.requireCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        return self.toggleFavoriteForUser(user, lesson);
    }

    @CacheEvict(value = "lessons", key = "'all'")
    @Transactional
    public boolean toggleFavoriteForUser(User user, Lesson lesson) {
        if (favoriteLessonRepository.existsByUserAndLesson(user, lesson)) {
            favoriteLessonRepository.deleteByUserAndLesson(user, lesson);
            lesson.setFavoriteCount(Math.max(0, lesson.getFavoriteCount() - 1));
            lessonRepository.save(lesson);
            return false;
        }
        favoriteLessonRepository.save(new FavoriteLesson(user, lesson, Instant.now()));
        lesson.setFavoriteCount(lesson.getFavoriteCount() + 1);
        lessonRepository.save(lesson);
        return true;
    }
}
