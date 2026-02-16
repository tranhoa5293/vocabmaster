package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.dto.UserVocabularyProgressItem;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.domain.entity.UserVocabulary;
import com.softfine.vocabmaster.domain.entity.UserVocabularyId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface UserVocabularyRepository extends JpaRepository<UserVocabulary, UserVocabularyId> {
    @Query("select uv from UserVocabulary uv join fetch uv.vocabulary v where uv.user = :user and (uv.nextReviewAt is null or uv.nextReviewAt <= :now)")
    List<UserVocabulary> findDueByUser(User user, Instant now, Pageable pageable);

    @Query("select uv from UserVocabulary uv join fetch uv.vocabulary v where uv.user = :user and (uv.nextReviewAt is null or uv.nextReviewAt <= :now) ORDER BY RANDOM()")
    List<UserVocabulary> findRandomDueByUser(User user, Instant now, Pageable pageable);

    @Query("""
            select new com.softfine.vocabmaster.domain.dto.UserVocabularyProgressItem(
                cast(uv.user.id as string),
                cast(uv.vocabulary.id as string),
                uv.word,
                uv.easeFactor,
                uv.interval,
                uv.repetition,
                uv.nextReviewAt,
                uv.lastResult,
                uv.totalCorrect,
                uv.totalWrong
            )
            from UserVocabulary uv
            where uv.user = :user
            """)
    Page<UserVocabularyProgressItem> findProgressByUser(User user, Pageable pageable);

    long countByUser(User user);

    @Query("select count(uv) from UserVocabulary uv where uv.user = :user and uv.vocabulary.lesson.id = :lessonId")
    long countByUserAndLessonId(User user, Long lessonId);

    @Query("select count(uv) from UserVocabulary uv where uv.user = :user and uv.vocabulary.lesson.collection.id = :collectionId")
    long countByUserAndCollectionId(User user, Long collectionId);

    @Query("select count(uv) from UserVocabulary uv where uv.user = :user and (uv.nextReviewAt is null or uv.nextReviewAt <= :now)")
    long countDueByUser(User user, Instant now);

    @Query("select count(uv) from UserVocabulary uv where uv.user = :user and uv.repetition >= :minRepetition")
    long countMasteredByUser(User user, int minRepetition);

    @Query("select uv from UserVocabulary uv join fetch uv.vocabulary v where uv.user = :user and v.lesson.id = :lessonId and (uv.nextReviewAt is null or uv.nextReviewAt <= :now)")
    List<UserVocabulary> findDueByUserAndLessonId(User user, Long lessonId, Instant now, Pageable pageable);

    @Query("select uv from UserVocabulary uv join fetch uv.vocabulary v where uv.user = :user and v.lesson.collection.id = :collectionId and (uv.nextReviewAt is null or uv.nextReviewAt <= :now)")
    List<UserVocabulary> findDueByUserAndCollectionId(User user, Long collectionId, Instant now, Pageable pageable);

    @Query("select uv from UserVocabulary uv join fetch uv.vocabulary v where uv.user = :user and v.lesson.collection.id = :collectionId and (uv.nextReviewAt is null or uv.nextReviewAt <= :now) order by random()")
    List<UserVocabulary> findRandomDueByUserAndCollectionId(User user, Long collectionId, Instant now, Pageable pageable);

    @Query("select uv from UserVocabulary uv join fetch uv.vocabulary v where uv.user = :user and v.lesson.id = :lessonId and (uv.nextReviewAt is null or uv.nextReviewAt <= :now) order by RANDOM()")
    List<UserVocabulary> findRandomDueByUserAndLessonId(User user, Long lessonId, Instant now, Pageable pageable);

}
