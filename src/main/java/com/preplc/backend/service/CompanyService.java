package com.preplc.backend.service;

import com.preplc.backend.model.Company;
import com.preplc.backend.model.CompanyQuestion;
import com.preplc.backend.repository.CompanyQuestionRepository;
import com.preplc.backend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyQuestionRepository companyQuestionRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Page<CompanyQuestion> getQuestionsByCompany(Long companyId, String timeFrame, Pageable pageable) {
        List<CompanyQuestion> allQuestions;
        
        if (timeFrame != null && !timeFrame.isEmpty() && !timeFrame.equalsIgnoreCase("all")) {
            allQuestions = companyQuestionRepository.findByCompanyIdAndTimeFrame(companyId, timeFrame);
        } else {
            allQuestions = companyQuestionRepository.findByCompanyId(companyId);
        }
        
        // Manual pagination for the filtered list
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allQuestions.size());
        
        List<CompanyQuestion> pageContent = allQuestions.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allQuestions.size());
    }
}

