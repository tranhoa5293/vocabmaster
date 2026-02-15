package com.softfine.vocabmaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "favorite_collections")
public class FavoriteCollection {

    @EmbeddedId
    private FavoriteCollectionId id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected FavoriteCollection() {
    }

    public FavoriteCollection(User user, Collection collection, Instant createdAt) {
        this.user = user;
        this.collection = collection;
        this.createdAt = createdAt;
        this.id = new FavoriteCollectionId(user.getId(), collection.getId());
    }

    public FavoriteCollectionId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Collection getCollection() {
        return collection;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
