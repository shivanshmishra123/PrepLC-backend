package com.preplc.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String difficulty;

    @Column(name = "acceptance_rate")
    private Float acceptanceRate;

    @OneToMany(mappedBy = "question")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("question")
    private java.util.List<CompanyQuestion> companyQuestions;
}
