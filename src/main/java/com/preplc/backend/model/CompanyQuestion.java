package com.preplc.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "company_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "time_frame", nullable = false, length = 50)
    private String timeFrame; // all, more-than-six-months, six-months, thirty-days, three-months

    @Column(name = "frequency")
    private Integer frequency;
}
