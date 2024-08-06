package com.example.testAi.subject;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int priority;

    private LocalDateTime createdDate;
    private LocalDateTime expiredDate;

    @Column(length = 200)
    private String subject;
    private String description;

    // Tree 구조를 위한 필드
    @ManyToOne
    private Subject parent;

    @OneToMany(cascade = CascadeType.REMOVE)
    List<Subject> children;

    private boolean isDone;
    private boolean isDivide;
}