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

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<CompanyQuestion> getQuestionsByCompany(Long companyId, String timeFrame, Pageable pageable) {
        Page<Long> idPage;
        
        if (timeFrame != null && !timeFrame.isEmpty() && !timeFrame.equalsIgnoreCase("all")) {
            idPage = companyQuestionRepository.findIdsByCompanyIdAndTimeFrame(companyId, timeFrame, pageable);
        } else {
            idPage = companyQuestionRepository.findIdsByCompanyId(companyId, pageable);
        }
        
        if (idPage.isEmpty()) {
            return new PageImpl<>(java.util.Collections.emptyList(), pageable, 0);
        }
        
        List<CompanyQuestion> content = companyQuestionRepository.findByIdsWithDetails(idPage.getContent());
        return new PageImpl<>(content, pageable, idPage.getTotalElements());
    }
}

