package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.LeaderboardResponse;
import com.softfine.vocabmaster.service.LeaderboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboard")
    public LeaderboardResponse getLeaderboard(@RequestParam(defaultValue = "weekly") String period) {
        return leaderboardService.getLeaderboard(period);
    }
}
