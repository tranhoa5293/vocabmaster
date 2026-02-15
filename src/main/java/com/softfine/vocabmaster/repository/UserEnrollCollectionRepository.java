package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.Collection;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.domain.entity.UserEnrollCollection;
import com.softfine.vocabmaster.domain.entity.UserEnrollCollectionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEnrollCollectionRepository extends JpaRepository<UserEnrollCollection, UserEnrollCollectionId> {
    boolean existsByUserAndCollection(User user, Collection collection);
}
