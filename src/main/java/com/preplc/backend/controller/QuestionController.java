package com.preplc.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.preplc.backend.model.Question;
import com.preplc.backend.service.CacheService;
import com.preplc.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CacheService cacheService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllQuestions(
            @RequestParam(required = false) String timeFrame,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        String tf = (timeFrame == null || timeFrame.isEmpty()) ? "all" : timeFrame;
        String cacheKey = String.format("preplc:questions:tf=%s:p=%d:s=%d", tf, page, size);

        Map<String, Object> cached = cacheService.get(cacheKey, new TypeReference<Map<String, Object>>() {});
        if (cached != null) {
            return ResponseEntity.ok(cached);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage;
        
        if (timeFrame != null && !timeFrame.equals("all")) {
            questionPage = questionService.getQuestionsByTimeFrame(timeFrame, pageable);
        } else {
            questionPage = questionService.getAllQuestions(pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("questions", questionPage.getContent());
        response.put("currentPage", questionPage.getNumber());
        response.put("totalPages", questionPage.getTotalPages());
        response.put("totalElements", questionPage.getTotalElements());
        response.put("hasMore", questionPage.hasNext());

        cacheService.set(cacheKey, response, 60); // Cache for 1 hour
        
        return ResponseEntity.ok(response);
    }
}

