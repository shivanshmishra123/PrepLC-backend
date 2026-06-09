package com.preplc.backend.repository;

import com.preplc.backend.model.CompanyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyQuestionRepository extends JpaRepository<CompanyQuestion, Long> {
    
    @Query("SELECT DISTINCT cq FROM CompanyQuestion cq " +
           "LEFT JOIN FETCH cq.company " +
           "LEFT JOIN FETCH cq.question q " +
           "LEFT JOIN FETCH q.companyQuestions qc " +
           "LEFT JOIN FETCH qc.company " +
           "WHERE cq.company.id = :companyId")
    List<CompanyQuestion> findByCompanyIdWithDetails(@Param("companyId") Long companyId);

    @Query("SELECT DISTINCT cq FROM CompanyQuestion cq " +
           "LEFT JOIN FETCH cq.company " +
           "LEFT JOIN FETCH cq.question q " +
           "LEFT JOIN FETCH q.companyQuestions qc " +
           "LEFT JOIN FETCH qc.company " +
           "WHERE cq.company.id = :companyId AND cq.timeFrame = :timeFrame")
    List<CompanyQuestion> findByCompanyIdAndTimeFrameWithDetails(
            @Param("companyId") Long companyId, 
            @Param("timeFrame") String timeFrame);

    List<CompanyQuestion> findByCompanyId(Long companyId);
    List<CompanyQuestion> findByCompanyIdAndTimeFrame(Long companyId, String timeFrame);
    List<CompanyQuestion> findByTimeFrame(String timeFrame);
}
