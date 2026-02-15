package com.softfine.vocabmaster.repository;

import com.softfine.vocabmaster.domain.entity.Collection;
import com.softfine.vocabmaster.domain.entity.FavoriteCollection;
import com.softfine.vocabmaster.domain.entity.FavoriteCollectionId;
import com.softfine.vocabmaster.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteCollectionRepository extends JpaRepository<FavoriteCollection, FavoriteCollectionId> {
    boolean existsByUserAndCollection(User user, Collection collection);
    void deleteByUserAndCollection(User user, Collection collection);

    @Query("select fc.collection.id from FavoriteCollection fc where fc.user = :user")
    List<Long> findFavoriteCollectionIds(User user);
}
