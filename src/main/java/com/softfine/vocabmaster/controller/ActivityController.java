package com.softfine.vocabmaster.controller;

import com.softfine.vocabmaster.domain.dto.ActiveLearnerResponse;
import com.softfine.vocabmaster.service.UserCurrentActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ActivityController {

    private final UserCurrentActivityService userCurrentActivityService;

    public ActivityController(UserCurrentActivityService userCurrentActivityService) {
        this.userCurrentActivityService = userCurrentActivityService;
    }

    @GetMapping("/active-learners")
    public List<ActiveLearnerResponse> getActiveLearners() {
        return userCurrentActivityService.getActiveLearners();
    }
}
