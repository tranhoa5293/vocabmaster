package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.dto.LeaderboardEntryResponse;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.domain.entity.UserScore;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
    @Query("""
            select new com.softfine.vocabmaster.domain.dto.LeaderboardEntryResponse(
                cast(us.user.id as string),
                us.user.name,
                sum(us.score)
            )
            from UserScore us
            where us.date between :startDate and :endDate
            group by us.user.id, us.user.name
            order by sum(us.score) desc
            """)
    List<LeaderboardEntryResponse> findTopUsers(int startDate, int endDate, Pageable pageable);

    @Query("""
            select sum(us.score) from UserScore us
            where us.user = :user and us.date between :startDate and :endDate
            """)
    Long findUserScore(User user, int startDate, int endDate);
}
