package com.preplc.backend.controller;

import com.preplc.backend.model.Company;
import com.preplc.backend.model.CompanyQuestion;
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

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{companyId}/questions")
    public ResponseEntity<Map<String, Object>> getCompanyQuestions(
            @PathVariable Long companyId,
            @RequestParam(required = false) String timeFrame,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<CompanyQuestion> questionPage = companyService.getQuestionsByCompany(companyId, timeFrame, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("questions", questionPage.getContent());
        response.put("currentPage", questionPage.getNumber());
        response.put("totalPages", questionPage.getTotalPages());
        response.put("totalElements", questionPage.getTotalElements());
        response.put("hasMore", questionPage.hasNext());
        
        return ResponseEntity.ok(response);
    }
}

