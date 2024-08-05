package com.example.testAi.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

}
