package com.softfine.vocabmaster.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardResponse {
    private String period;
    private int startDate;
    private int endDate;
    private List<LeaderboardEntryResponse> topUsers;
    private long userScore;
}
