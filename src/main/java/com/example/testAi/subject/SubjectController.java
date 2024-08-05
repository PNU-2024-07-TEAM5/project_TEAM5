package com.example.testAi.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;


    @GetMapping("{id}")
    public String getTargetSubject(Model model, @PathVariable Long id) {
        model.addAttribute("mainSubject", subjectService.getSubject(id));
        return "test";
    }


    @GetMapping("{id}/divide")
    public String divideTargetSubject(Model model, @PathVariable Long id) {
        Optional<Subject> subject = subjectService.getSubject(id);
        if (subject.isEmpty()) {
            return "test";
        }
        model.addAttribute("subjects", subjectService.divide(id));
        model.addAttribute("openDivideForm", true);
        return "test";
    }

    @PostMapping("{id}/new")
    public String newSubjectFromTarget(@PathVariable Long id, SubjectForm subjectForm) {
        if
        return "redirect:/subject/" + id;
    }
}