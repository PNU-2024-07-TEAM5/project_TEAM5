package com.example.testAi.subject;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectForm {
    private String subject;
    private String description;
    private int expectDate;
    private int priority;
    private boolean isNeed;

}