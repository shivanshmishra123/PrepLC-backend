package com.preplc.backend.repository;

import com.preplc.backend.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Load IDs with pagination first, then fetch full data — avoids HHH90003004 warning
    @Query("SELECT DISTINCT q.id FROM Question q")
    Page<Integer> findAllIds(Pageable pageable);

    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.companyQuestions cq LEFT JOIN FETCH cq.company WHERE q.id IN :ids")
    List<Question> findByIdsWithCompanies(List<Integer> ids);
}
