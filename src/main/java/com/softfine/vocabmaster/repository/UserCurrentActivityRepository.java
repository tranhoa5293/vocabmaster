package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.UserCurrentActivity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface UserCurrentActivityRepository extends JpaRepository<UserCurrentActivity, Long> {
    @Query("select ua from UserCurrentActivity ua where ua.lastActiveAt >= :since order by ua.lastActiveAt desc")
    List<UserCurrentActivity> findActiveSince(Instant since, Pageable pageable);
}
