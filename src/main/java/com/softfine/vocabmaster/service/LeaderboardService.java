package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.dto.LeaderboardEntryResponse;
import com.softfine.vocabmaster.domain.dto.LeaderboardResponse;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.repository.UserScoreRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LeaderboardService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private final UserScoreRepository userScoreRepository;
    private final CurrentUserService currentUserService;
    private final LeaderboardService self;

    public LeaderboardService(UserScoreRepository userScoreRepository, CurrentUserService currentUserService, @Lazy LeaderboardService self) {
        this.userScoreRepository = userScoreRepository;
        this.currentUserService = currentUserService;
        this.self = self;
    }

    public LeaderboardResponse getLeaderboard(String period) {
        PeriodRange range = resolvePeriod(period);
        User user = currentUserService.requireCurrentUser();
        List<LeaderboardEntryResponse> topUsers = this.self.getTopUsers(period, range);
        long score = this.self.getUserScore(user, range);

        return new LeaderboardResponse(period, range.startDate(), range.endDate(), topUsers, score);
    }

    @Cacheable(value = "leaderboard-top", key = "#period")
    public List<LeaderboardEntryResponse> getTopUsers(String period, PeriodRange range) {
        return userScoreRepository.findTopUsers(
                range.startDate(), range.endDate(), PageRequest.of(0, 10));
    }

    @Cacheable(value = "leaderboard-user", key = "#user.id + ':' + #range.startDate + ':' + #range.endDate")
    public long getUserScore(User user, PeriodRange range) {
        Long userScore = userScoreRepository.findUserScore(user, range.startDate(), range.endDate());
        return userScore == null ? 0 : userScore;
    }

    private PeriodRange resolvePeriod(String period) {
        if (period == null || period.isBlank() || "weekly".equalsIgnoreCase(period)) {
            LocalDate today = LocalDate.now();
            LocalDate start = today.with(DayOfWeek.MONDAY);
            LocalDate end = start.plusDays(6);
            return new PeriodRange(toDateInt(start), toDateInt(end));
        }
        LocalDate today = LocalDate.now();
        LocalDate start = today.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);
        return new PeriodRange(toDateInt(start), toDateInt(end));
    }

    private int toDateInt(LocalDate date) {
        return Integer.parseInt(date.format(DATE_FORMAT));
    }

    public record PeriodRange(int startDate, int endDate) {}
}
