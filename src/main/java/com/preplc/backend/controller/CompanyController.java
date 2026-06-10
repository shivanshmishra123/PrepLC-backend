package com.preplc.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.preplc.backend.model.Company;
import com.preplc.backend.model.CompanyQuestion;
import com.preplc.backend.service.CacheService;
import com.preplc.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CacheService cacheService;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        String cacheKey = "preplc:companies:all";
        List<Company> cached = cacheService.get(cacheKey, new TypeReference<List<Company>>() {});
        if (cached != null) {
            return ResponseEntity.ok(cached);
        }

        List<Company> companies = companyService.getAllCompanies();
        cacheService.set(cacheKey, companies, 1440); // Cache for 24 hours
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{companyId}/questions")
    public ResponseEntity<Map<String, Object>> getCompanyQuestions(
            @PathVariable Long companyId,
            @RequestParam(required = false) String timeFrame,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        String tf = (timeFrame == null || timeFrame.isEmpty()) ? "all" : timeFrame;
        String cacheKey = String.format("preplc:company_questions:c=%d:tf=%s:p=%d:s=%d", companyId, tf, page, size);
        
        Map<String, Object> cached = cacheService.get(cacheKey, new TypeReference<Map<String, Object>>() {});
        if (cached != null) {
            return ResponseEntity.ok(cached);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<CompanyQuestion> questionPage = companyService.getQuestionsByCompany(companyId, tf, pageable);
        
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

