package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.constant.Constant;
import com.softfine.vocabmaster.domain.dto.ProgressPageResponse;
import com.softfine.vocabmaster.domain.dto.StudyStatsResponse;
import com.softfine.vocabmaster.domain.dto.VocabularyImportRequest;
import com.softfine.vocabmaster.domain.dto.VocabularyResponse;
import com.softfine.vocabmaster.domain.entity.*;
import com.softfine.vocabmaster.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final CollectionRepository collectionRepository;
    private final LessonRepository lessonRepository;
    private final UserEnrollCollectionRepository userEnrollCollectionRepository;
    private final UserEnrollRepository userEnrollRepository;
    private final VocabularyRepository vocabularyRepository;
    private final UserVocabularyRepository userVocabularyRepository;
    private final CurrentUserService currentUserService;
    private final UserCurrentActivityService userCurrentActivityService;
    private final LessonService lessonService;

    @Transactional
    public List<VocabularyResponse> getVocabulary(Long lessonId) {
        User user = currentUserService.requireCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        enrollIfNeeded(user, lessonId);
        return vocabularyRepository.findByLesson(lesson).stream()
                .map(VocabularyResponse::from)
                .toList();
    }

    public List<VocabularyResponse> importVocabulary(Long lessonId, List<VocabularyImportRequest> items) {
        User user = currentUserService.requireCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        List<Vocabulary> saved = items.stream()
                .map(item ->  Vocabulary.builder().lesson(lesson)
                                .word(item.getWord()).meaning(item.getMeaning()).pronunciation(item.getPronunciation())
                                .example(item.getExample()).build()
                )
                .map(vocabularyRepository::save)
                .toList();

        lesson.setTotalWord(vocabularyRepository.countByLessonId(lessonId));
        lessonRepository.save(lesson);
        lessonService.evictCache(lesson.getCollection().getId());
        return saved.stream().map(VocabularyResponse::from).toList();
    }

    @Transactional
    public List<VocabularyResponse> getDueVocabulary(int limit, Long lessonId, Long collectionId, String mode) {
        User user = currentUserService.requireCurrentUser();
        Instant now = Instant.now();
        userCurrentActivityService.recordActivity(lessonId, mode);
//
//        if (lessonId == null || userVocabularyRepository.countByUserAndLessonId(user, lessonId) == 0) {
//            initUserVocabulary(user, lessonId, now);
//        }
        List<UserVocabulary> userVocabularies;
        if (lessonId == null && collectionId == null) {
            if (Constant.MODE_MATCH.equals(mode)) {
                userVocabularies = userVocabularyRepository.findRandomDueByUser(user, now, PageRequest.of(0, limit));
            } else {
                userVocabularies = userVocabularyRepository.findDueByUser(user, now, PageRequest.of(0, limit));
            }
        } else if (lessonId != null) {
            if (Constant.MODE_MATCH.equals(mode)) {
                userVocabularies = userVocabularyRepository.findRandomDueByUserAndLessonId(user, lessonId, now, PageRequest.of(0, limit));
            } else {
                userVocabularies = userVocabularyRepository.findDueByUserAndLessonId(user, lessonId, now, PageRequest.of(0, limit));
            }
        } else {
            if (Constant.MODE_MATCH.equals(mode)) {
                userVocabularies = userVocabularyRepository.findRandomDueByUserAndCollectionId(user, collectionId, now, PageRequest.of(0, limit));
            } else {
                userVocabularies = userVocabularyRepository.findDueByUserAndCollectionId(user, collectionId, now, PageRequest.of(0, limit));
            }
        }
        List<VocabularyResponse> vocabularyResponses = new java.util.ArrayList<>(userVocabularies.stream()
                .map(UserVocabulary::getVocabulary)
                .map(VocabularyResponse::from)
                .toList());
        if(userVocabularies.isEmpty() && lessonId != null){
            enrollIfNeeded(user, lessonId);
        }
        List<Vocabulary> vocabularies;
        if (userVocabularies.size() < limit) {
            if(lessonId != null){
                vocabularies = vocabularyRepository.findNewWord(user.getId(), lessonId,limit - userVocabularies.size());
            } else if (collectionId != null) {
                vocabularies = vocabularyRepository.findNewWordByCollection(user.getId(), collectionId,limit - userVocabularies.size());
            } else {
                vocabularies = vocabularyRepository.findNewWord(user.getId(),limit - userVocabularies.size());
            }
            if(!vocabularies.isEmpty()){
                List<VocabularyResponse> newVocabularyResponses = vocabularies.stream()
                        .map(VocabularyResponse::from)
                        .toList();
                vocabularyResponses.addAll(newVocabularyResponses);
            }
        }
        return vocabularyResponses;
    }

    public ProgressPageResponse getUserProgress(int page, int size) {
        User user = currentUserService.requireCurrentUser();
        var pageResult = userVocabularyRepository.findProgressByUser(user, PageRequest.of(page, size));
        return new ProgressPageResponse(
                pageResult.getContent(),
                pageResult.getTotalPages(),
                pageResult.getNumber()
        );
    }

    public StudyStatsResponse getStudyStats() {
        User user = currentUserService.requireCurrentUser();
        long total = vocabularyRepository.count();
        long due = total - userVocabularyRepository.countByUser(user) + userVocabularyRepository.countDueByUser(user, Instant.now());
        long mastered = userVocabularyRepository.countMasteredByUser(user, 5);
        return new StudyStatsResponse(total, due, mastered);
    }

    private void initUserVocabulary(User user, Long lessonId, Instant now) {
        List<Vocabulary> vocabularies = vocabularyRepository.findByLessonId(lessonId == null ? Constant.DEFAULT_LESSON_ID : lessonId);
        if (vocabularies == null || vocabularies.isEmpty()) {
            return;
        }
        List<UserVocabulary> items = vocabularies.stream()
                .map(vocab -> {
                    UserVocabulary uv = new UserVocabulary(user, vocab);
                    uv.setNextReviewAt(now);
                    return uv;
                })
                .toList();
        enrollIfNeeded(user, lessonId);
        userVocabularyRepository.saveAll(items);
    }

    private void enrollIfNeeded(User user, Long lessonId) {
        if(lessonId == null){
            return;
        }
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if(lessonOptional.isEmpty()){
            return;
        }
        Lesson lesson = lessonOptional.get();
        if (userEnrollRepository.existsByUserAndLesson(user, lesson)) {
            return;
        }
        userEnrollRepository.save(new UserEnroll(user, lesson, Instant.now()));
        lesson.setEnrollCount(lesson.getEnrollCount() + 1);
        lessonRepository.save(lesson);

        enrollCollectionIfNeeded(user, lesson.getCollection());
    }

    private void enrollCollectionIfNeeded(User user, Collection collection) {
        if (userEnrollCollectionRepository.existsByUserAndCollection(user, collection)) {
            return;
        }
        userEnrollCollectionRepository.save(new com.softfine.vocabmaster.domain.entity.UserEnrollCollection(
                user,
                collection,
                Instant.now()
        ));
        collection.setEnrollCount(collection.getEnrollCount() + 1);
        collectionRepository.save(collection);
    }
}
