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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private CompanyQuestionRepository companyQuestionRepository;

    public Page<Question> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }
    
    public Page<Question> getQuestionsByTimeFrame(String timeFrame, Pageable pageable) {
        List<CompanyQuestion> companyQuestions = companyQuestionRepository.findByTimeFrame(timeFrame);
        List<Question> questions = companyQuestions.stream()
                .map(CompanyQuestion::getQuestion)
                .distinct()
                .collect(Collectors.toList());
        
        // Manual pagination for the filtered list
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), questions.size());
        
        List<Question> pageContent = questions.subList(start, end);
        return new PageImpl<>(pageContent, pageable, questions.size());
    }
}

