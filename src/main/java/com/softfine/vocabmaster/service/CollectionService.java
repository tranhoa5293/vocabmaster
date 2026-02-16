package com.softfine.vocabmaster.service;

import com.softfine.vocabmaster.domain.dto.CollectionRequest;
import com.softfine.vocabmaster.domain.dto.CollectionResponse;
import com.softfine.vocabmaster.domain.entity.Collection;
import com.softfine.vocabmaster.domain.entity.FavoriteCollection;
import com.softfine.vocabmaster.domain.entity.User;
import com.softfine.vocabmaster.repository.CollectionRepository;
import com.softfine.vocabmaster.repository.FavoriteCollectionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final FavoriteCollectionRepository favoriteCollectionRepository;
    private final CurrentUserService currentUserService;
    private final CollectionService self;

    public CollectionService(CollectionRepository collectionRepository,
                             FavoriteCollectionRepository favoriteCollectionRepository,
                             CurrentUserService currentUserService,
                             @Lazy CollectionService self) {
        this.collectionRepository = collectionRepository;
        this.favoriteCollectionRepository = favoriteCollectionRepository;
        this.currentUserService = currentUserService;
        this.self = self;
    }

    public List<CollectionResponse> getCollections() {
        User user = currentUserService.requireCurrentUser();
        List<CollectionResponse> collectionResponses = self.getCollectionsCached();
        var favorites = new HashSet<>(favoriteCollectionRepository.findFavoriteCollectionIds(user));
        return collectionResponses.stream()
                .peek(response -> response.setFavorite(favorites.contains(Long.valueOf(response.getId()))))
                .sorted((a, b) -> {
                    if (a.isFavorite() == b.isFavorite()) {
                        return 0;
                    }
                    return a.isFavorite() ? -1 : 1;
                })
                .toList();
    }

    public CollectionResponse saveCollection(CollectionRequest request) {
        User user = currentUserService.requireCurrentUser();
        return self.saveCollectionForUser(user, request);
    }

    @Cacheable(value = "collections", key = "'all'")
    public List<CollectionResponse> getCollectionsCached() {
        return collectionRepository.findAll().stream()
                .map(collection -> CollectionResponse.from(collection, false))
                .toList();
    }

    @CacheEvict(value = "collections", key = "'all'")
    public CollectionResponse saveCollectionForUser(User user, CollectionRequest request) {
        Collection collection = Collection.builder()
                .user(user)
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();
        return CollectionResponse.from(collectionRepository.save(collection), false);
    }

    public boolean toggleFavorite(Long collectionId) {
        User user = currentUserService.requireCurrentUser();
        return self.toggleFavoriteForUser(user, collectionId);
    }

    @CacheEvict(value = "collections", key = "'all'")
    @Transactional
    public boolean toggleFavoriteForUser(User user, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new IllegalArgumentException("Collection not found"));

        if (favoriteCollectionRepository.existsByUserAndCollection(user, collection)) {
            favoriteCollectionRepository.deleteByUserAndCollection(user, collection);
            collection.setFavoriteCount(Math.max(0, collection.getFavoriteCount() - 1));
            collectionRepository.save(collection);
            return false;
        }
        favoriteCollectionRepository.save(new FavoriteCollection(
                user,
                collection,
                Instant.now()
        ));
        collection.setFavoriteCount(collection.getFavoriteCount() + 1);
        collectionRepository.save(collection);
        return true;
    }
}
