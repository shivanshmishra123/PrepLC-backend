package com.preplc.backend.repository;

import com.preplc.backend.model.CompanyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyQuestionRepository extends JpaRepository<CompanyQuestion, Long> {
    List<CompanyQuestion> findByCompanyId(Long companyId);
    List<CompanyQuestion> findByCompanyIdAndTimeFrame(Long companyId, String timeFrame);
    List<CompanyQuestion> findByTimeFrame(String timeFrame);
}
