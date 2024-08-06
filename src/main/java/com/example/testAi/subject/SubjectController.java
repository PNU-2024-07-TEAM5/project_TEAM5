package com.example.testAi.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;


    @GetMapping("main")
    public String getAllSubjects(Model model) {
        if (model.getAttribute("openDivideForm") == null) {
            model.addAttribute("openDivideForm", false);
        }
        if (model.getAttribute("subjectForms") == null) {
            model.addAttribute("subjectForms", new ArrayList<SubjectForm>());
        }

        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "home_test";
    }

    @PostMapping("main/divide")
    public String divideSubject(@RequestParam Long id, RedirectAttributes model) {

        List<SubjectForm> subjectForms = subjectService.divide(id);

        if (subjectForms == null) {
            return "redirect:/subject/main";
        }

        model.addFlashAttribute("openDivideForm", true);
        model.addFlashAttribute("subjectForms", subjectForms );
        return "redirect:/subject/main";
    }


    @GetMapping("list/{id}")
    public String getTargetSubject(Model model, @PathVariable Long id) {
        Subject mainSubject = subjectService.getSubject(id).orElse(null);
        if (mainSubject == null) {
            return "redirect:/subject/main";
        }

        if (model.getAttribute("openDivideForm") == null) {
            model.addAttribute("openDivideForm", false);
        }

        model.addAttribute("target", mainSubject);
        model.addAttribute("subjects", mainSubject.getChildren());
        return "test";
    }


    @PostMapping("{id}/new")
    public String newSubjectFromTarget(@PathVariable Long id, @RequestParam String input) {
        subjectService.addSubjectTO(id, input);
        return "redirect:/subject/" + id;
    }

}