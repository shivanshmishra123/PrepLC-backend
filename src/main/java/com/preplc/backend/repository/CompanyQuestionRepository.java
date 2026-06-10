package com.preplc.backend.repository;

import com.preplc.backend.model.CompanyQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyQuestionRepository extends JpaRepository<CompanyQuestion, Long> {
    
    @Query("SELECT DISTINCT cq.id FROM CompanyQuestion cq WHERE cq.company.id = :companyId")
    Page<Long> findIdsByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT DISTINCT cq.id FROM CompanyQuestion cq WHERE cq.company.id = :companyId AND cq.timeFrame = :timeFrame")
    Page<Long> findIdsByCompanyIdAndTimeFrame(
            @Param("companyId") Long companyId, 
            @Param("timeFrame") String timeFrame, 
            Pageable pageable);

    @Query("SELECT DISTINCT cq FROM CompanyQuestion cq " +
           "LEFT JOIN FETCH cq.company " +
           "LEFT JOIN FETCH cq.question q " +
           "LEFT JOIN FETCH q.companyQuestions qc " +
           "LEFT JOIN FETCH qc.company " +
           "WHERE cq.id IN :ids")
    List<CompanyQuestion> findByIdsWithDetails(@Param("ids") List<Long> ids);

    List<CompanyQuestion> findByCompanyId(Long companyId);
    List<CompanyQuestion> findByCompanyIdAndTimeFrame(Long companyId, String timeFrame);
    List<CompanyQuestion> findByTimeFrame(String timeFrame);
}
