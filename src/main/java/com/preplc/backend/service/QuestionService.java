package com.preplc.backend.service;

import com.preplc.backend.model.Question;
import com.preplc.backend.model.CompanyQuestion;
import com.preplc.backend.repository.QuestionRepository;
import com.preplc.backend.repository.CompanyQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private CompanyQuestionRepository companyQuestionRepository;

    @Transactional(readOnly = true)
    public Page<Question> getAllQuestions(Pageable pageable) {
        // Step 1: fetch paginated IDs only (avoids Hibernate pagination + JOIN FETCH warning)
        Page<Integer> idPage = questionRepository.findAllIds(pageable);

        // Step 2: fetch full Question + companyQuestions + company in one JOIN FETCH query
        List<Question> questions = questionRepository.findByIdsWithCompanies(idPage.getContent());

        return new PageImpl<>(questions, pageable, idPage.getTotalElements());
    }
    
    @Transactional(readOnly = true)
    public Page<Question> getQuestionsByTimeFrame(String timeFrame, Pageable pageable) {
        // Step 1: fetch paginated IDs only for the given timeframe
        Page<Integer> idPage = questionRepository.findIdsByTimeFrame(timeFrame, pageable);

        if (idPage.isEmpty()) {
            return new PageImpl<>(java.util.Collections.emptyList(), pageable, 0);
        }

        // Step 2: fetch full Question + companyQuestions + company in one JOIN FETCH query
        List<Question> questions = questionRepository.findByIdsWithCompanies(idPage.getContent());

        return new PageImpl<>(questions, pageable, idPage.getTotalElements());
    }
}
