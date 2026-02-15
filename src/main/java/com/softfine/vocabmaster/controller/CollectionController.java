package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.CollectionRequest;
import com.softfine.vocabmaster.domain.dto.CollectionResponse;
import com.softfine.vocabmaster.service.CollectionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collections")
@AllArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping
    public List<CollectionResponse> getCollections() {
        return collectionService.getCollections();
    }

    @PostMapping
    public CollectionResponse saveCollection(@RequestBody CollectionRequest request) {
        return collectionService.saveCollection(request);
    }
}
