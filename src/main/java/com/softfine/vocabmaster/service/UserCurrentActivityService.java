package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.dto.ActiveLearnerResponse;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.domain.entity.UserCurrentActivity;
import com.softfine.vocabmaster.repository.UserCurrentActivityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserCurrentActivityService {

    private final UserCurrentActivityRepository userCurrentActivityRepository;
    private final CurrentUserService currentUserService;

    @Value("${app.active-window-minutes:10}")
    private int activeWindowMinutes;

    public UserCurrentActivityService(UserCurrentActivityRepository userCurrentActivityRepository,
                                      CurrentUserService currentUserService) {
        this.userCurrentActivityRepository = userCurrentActivityRepository;
        this.currentUserService = currentUserService;
    }

    public void recordActivity(Long currentLessonId, String currentMode) {
        User user = currentUserService.requireCurrentUser();
        Instant now = Instant.now();
        UserCurrentActivity activity = userCurrentActivityRepository.findById(user.getId())
                .orElse(new UserCurrentActivity(user.getId(), user.getName(), now, currentLessonId, currentMode));
        activity.setUserName(user.getName());
        activity.setLastActiveAt(now);
        activity.setCurrentLessonId(currentLessonId);
        activity.setCurrentMode(currentMode);
        userCurrentActivityRepository.save(activity);
    }

    public List<ActiveLearnerResponse> getActiveLearners() {
        Instant since = Instant.now().minusSeconds(activeWindowMinutes * 60L);
        return userCurrentActivityRepository.findActiveSince(since, PageRequest.of(0, 5)).stream()
                .map(activity -> new ActiveLearnerResponse(
                        activity.getUserId().toString(),
                        activity.getUserName(),
                        activity.getLastActiveAt(),
                        activity.getCurrentLessonId(),
                        activity.getCurrentMode()
                ))
                .toList();
    }
}
