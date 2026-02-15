package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.Lesson;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.domain.entity.UserEnroll;
import com.softfine.vocabmaster.domain.entity.UserEnrollId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEnrollRepository extends JpaRepository<UserEnroll, UserEnrollId> {
    boolean existsByUserAndLesson(User user, Lesson lesson);
}
