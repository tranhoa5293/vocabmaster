package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.entity.UserScore;
import com.softfine.vocabmaster.domain.entity.UserVocabulary;
import com.softfine.vocabmaster.domain.entity.UserVocabularyId;
import com.softfine.vocabmaster.domain.entity.Vocabulary;
import com.softfine.vocabmaster.repository.UserScoreRepository;
import com.softfine.vocabmaster.repository.UserVocabularyRepository;
import com.softfine.vocabmaster.repository.VocabularyRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

import com.softfine.vocabmaster.domain.dto.SrsBatchUpdateItem;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SrsService {

    private final UserVocabularyRepository userVocabularyRepository;
    private final VocabularyRepository vocabularyRepository;
    private final CurrentUserService currentUserService;
    private final UserScoreRepository userScoreRepository;

    public SrsService(UserVocabularyRepository userVocabularyRepository,
                      VocabularyRepository vocabularyRepository,
                      CurrentUserService currentUserService,
                      UserScoreRepository userScoreRepository) {
        this.userVocabularyRepository = userVocabularyRepository;
        this.vocabularyRepository = vocabularyRepository;
        this.currentUserService = currentUserService;
        this.userScoreRepository = userScoreRepository;
    }

    @CacheEvict(value = {"leaderboard-top", "leaderboard-user"}, allEntries = true)
    public UserVocabulary updateSrs(String vocabId, int score) {
        if(score < 0 || score > 5) {
            throw new IllegalArgumentException("Score must be between 0 and 5");
        }
        var user = currentUserService.requireCurrentUser();
        Long vocabLong = Long.valueOf(vocabId);
        Vocabulary vocabulary = vocabularyRepository.findById(vocabLong)
                .orElseThrow(() -> new IllegalArgumentException("Vocabulary not found: " + vocabLong));

        UserVocabulary userVocabulary = userVocabularyRepository.findById(new UserVocabularyId(user.getId(), vocabLong))
                .orElseGet(() -> userVocabularyRepository.save(new UserVocabulary(user, vocabulary)));

        applySm2(userVocabulary, score);
        userScoreRepository.save(new UserScore(
                user,
                score,
                vocabulary.getWord(),
                Integer.parseInt(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE)),
                java.time.Instant.now()
        ));
        return userVocabularyRepository.save(userVocabulary);
    }

    @CacheEvict(value = {"leaderboard-top", "leaderboard-user"}, allEntries = true)
    public List<UserVocabulary> updateSrsBatch(List<SrsBatchUpdateItem> updates) {
        if (updates == null || updates.isEmpty()) {
            return List.of();
        }
        return updates.stream()
                .map(item -> updateSrs(item.getVocabId(), item.getScore()))
                .toList();
    }

    private void applySm2(UserVocabulary uv, int score) {
        int clamped = Math.max(0, Math.min(5, score));
        boolean correct = clamped >= 3;

        if (correct) {
            uv.setTotalCorrect(uv.getTotalCorrect() + 1);
            uv.setRepetition(uv.getRepetition() + 1);
            if (uv.getRepetition() == 1) {
                uv.setInterval(1);
            } else if (uv.getRepetition() == 2) {
                uv.setInterval(6);
            } else {
                uv.setInterval((int) Math.round(uv.getInterval() * uv.getEaseFactor()));
            }

            double ef = uv.getEaseFactor();
            double newEf = ef + (0.1 - (5 - clamped) * (0.08 + (5 - clamped) * 0.02));
            uv.setEaseFactor(Math.max(1.3, newEf));
            uv.setLastResult("correct");
        } else {
            uv.setTotalWrong(uv.getTotalWrong() + 1);
            uv.setRepetition(0);
            uv.setInterval(1);
            uv.setEaseFactor(Math.max(1.3, uv.getEaseFactor() - 0.2));
            uv.setLastResult("wrong");
        }

        uv.setNextReviewAt(Instant.now().plus(uv.getInterval(), ChronoUnit.DAYS));
    }
}
