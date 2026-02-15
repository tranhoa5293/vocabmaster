package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserEnrollCollectionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "collection_id")
    private Long collectionId;

    protected UserEnrollCollectionId() {
    }

    public UserEnrollCollectionId(Long userId, Long collectionId) {
        this.userId = userId;
        this.collectionId = collectionId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEnrollCollectionId that = (UserEnrollCollectionId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(collectionId, that.collectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, collectionId);
    }
}
