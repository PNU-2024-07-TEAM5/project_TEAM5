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


    @GetMapping("main")
    public String getAllSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "home_test";
    }


    @GetMapping("{id}")
    public String getTargetSubject(Model model, @PathVariable Long id) {
        Subject mainSubject = subjectService.getSubject(id).orElse(null);
        if (mainSubject == null) {
            return String.format("redirect:/subject/list/%d", id);
        }
        model.addAttribute("target", mainSubject);
        model.addAttribute("subjects", mainSubject.getChildren());

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

    @PostMapping("/list{id}/new")
    public String newSubjectFromTarget(@PathVariable Long id, SubjectForm subjectForm) {
        return "redirect:/subject/" + id;
    }
}