package com.example.testAi.subject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectFormScope subjectFormScope;


    @GetMapping("main")
    public String getAllSubjects(Model model) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.DIVIDE)){
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.START);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.CHECK);
        }

        Long requestedId = subjectFormScope.getRequestedId();
        List<SubjectForm> subjectForms = new ArrayList<>(subjectFormScope.getSubjectForms());
        model.addAttribute("requestedId", requestedId);
        model.addAttribute("subjectForms", subjectForms);
        model.addAttribute("subjects", subjectService.getAll());
        return "home_test";
    }

    @PostMapping("main/divide")
    public String divideSubject(@RequestParam("id") Long id, RedirectAttributes model) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.START)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.NONE);
            return "redirect:/subject/main";
        } else {
            subjectFormScope.setPrevStep(ControllerStep.DIVIDE);
        }

        subjectFormScope.clear();
        subjectFormScope.setRequestedId(id);
        subjectFormScope.setSubjectForms(subjectService.divide(id));

        model.addFlashAttribute("requestedId", subjectFormScope.getRequestedId());
        model.addFlashAttribute("subjectForms", subjectFormScope.getSubjectForms());
        return "redirect:/subject/main";
    }

    @PostMapping("main/save")
    public String saveSubjects(@RequestParam("body") String body) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.CHECK)) {
           subjectFormScope.clear();
           subjectFormScope.setPrevStep(ControllerStep.NONE);
           return "redirect:/subject/main";
        } else {
            subjectFormScope.setPrevStep(ControllerStep.SAVE);
        }

        JSONObject jsonObject = new JSONObject(body);
        HashSet<Integer> parsedSelect = jsonObject.getJSONArray("subjectNo").toList()
                .stream().map(Objects::toString).map(Integer::parseInt).collect(Collectors.toCollection(HashSet::new));

        List<SubjectForm> selectedForm = subjectFormScope.getSubjectForms().stream()
                .filter(subjectForm -> parsedSelect.contains(subjectForm.getNo()))
                .toList();

        Subject target = subjectService.get(subjectFormScope.getRequestedId()).orElse(null);
        subjectService.save(target, selectedForm);
        subjectFormScope.clear();
        return "redirect:/subject/main";
    }


    @PostMapping("main/add")
    public String addSubject(@RequestParam("taskInput") String taskInput) {
        subjectService.add(null, taskInput);
        return "redirect:/subject/main";
    }


    @GetMapping("{sid}")
    public String getTargetSubjects(Model model, @PathVariable Long sid) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.DIVIDE)){
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.START);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.CHECK);
        }

        Long requestedId = subjectFormScope.getRequestedId();
        List<SubjectForm> subjectForms = new ArrayList<>(subjectFormScope.getSubjectForms());
        Subject target = subjectService.get(sid).orElse(null);
        if (target == null) {
            return "redirect:/subject/main";
        }

        model.addAttribute("requestedId", requestedId);
        model.addAttribute("subjectForms", subjectForms);
        model.addAttribute("target", target);
        model.addAttribute("subjects", target.getChildren());

        return "test";
    }

    @PostMapping("{sid}/divide")
    public String divideTargetSubject(@RequestParam("id") Long id, RedirectAttributes model, @PathVariable Long sid) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.START)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.START);
            return String.format("redirect:/subject/%d", sid);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.DIVIDE);
        }
        subjectFormScope.clear();
        subjectFormScope.setRequestedId(id);
        subjectFormScope.setSubjectForms(subjectService.divide(id));

        model.addFlashAttribute("requestedId", subjectFormScope.getRequestedId());
        model.addFlashAttribute("subjectForms", subjectFormScope.getSubjectForms());
        return String.format("redirect:/subject/%d", sid);
    }

    @PostMapping("{sid}/save")
    public String saveTargetSubjects(@RequestParam("body") String body, @PathVariable Long sid) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.CHECK)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.NONE);
            return String.format("redirect:/subject/%d", sid);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.SAVE);
        }

        JSONObject jsonObject = new JSONObject(body);
        HashSet<Integer> parsedSelect = jsonObject.getJSONArray("subjectNo").toList()
                .stream().map(Objects::toString).map(Integer::parseInt).collect(Collectors.toCollection(HashSet::new));

        List<SubjectForm> selectedForm = subjectFormScope.getSubjectForms().stream()
                .filter(subjectForm -> parsedSelect.contains(subjectForm.getNo()))
                .toList();

        Subject target = subjectService.get(subjectFormScope.getRequestedId()).orElse(null);
        subjectService.save(target, selectedForm);
        subjectFormScope.clear();
        return String.format("redirect:/subject/%d", sid);
    }

    @PostMapping("{sid}/add")
    public String addTargetSubject(@RequestParam("taskInput") String taskInput, @PathVariable Long sid) {
        subjectService.add(sid, taskInput);
        return String.format("redirect:/subject/%d", sid);
    }
}