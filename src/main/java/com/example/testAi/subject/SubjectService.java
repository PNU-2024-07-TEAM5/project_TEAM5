package com.example.testAi.subject;


import com.example.testAi.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ChatService chatService;

    List<SubjectForm> divide(Long id) {
        //TODO : divide subjects
        StringBuilder sb = new StringBuilder();
        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null) {
            return null;
        }
        sb.append(subject.getSubject());
        Subject parent = subject.getParent();

        for (int i = 0; i < 3; ++i) { // 3번까지만 부모를 타고 올라가도록
            if (subject.getParent() != null) {
                sb.insert(0, parent.getSubject() + " > ");
                parent = parent.getParent();
            } else {
                break;
            }
        }

        var response = chatService.generate(sb.toString());
        Map<String, Object> map = ChatService.jsonConverter(response);
        if (map.get("Error") != null) {
            return null;
        }
        List<SubjectForm> subjects = new ArrayList<>();
        for (Map<String, Object> m : (List<Map<String, Object>>) map.get("subjects")) {
            SubjectForm subjectForm = new SubjectForm();
            subjectForm.setSubject((String) m.getOrDefault("subject", "Nothing"));
            subjectForm.setDescription((String) m.getOrDefault("description", "Nothing"));
            subjectForm.setExpectDate((Integer) m.getOrDefault("expect_date", 0));
            subjectForm.setPriority((Integer) m.getOrDefault("priority", 0));
            subjects.add(subjectForm);
        }
        subjects.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return subjects;
    }

    void saveCheckedSubjects(Subject parent, List<SubjectForm> subjects) {
        //TODO : save subject

        // form이 비어있으면 divide 상태가 되지 않은 것으로 간주
        if (subjects.isEmpty()) {
            return;
        }
        for (SubjectForm subjectForm : subjects) {
            parent.setDivide(true);
            if (subjectForm.isNeed()) {
                Subject subject = new Subject();
                subject.setSubject(subjectForm.getSubject());
                subject.setDescription(subjectForm.getDescription());
                subject.setCreatedDate(LocalDateTime.now());
                subject.setExpiredDate(LocalDateTime.now().plusDays(subjectForm.getExpectDate()));
                subject.setParent(parent);
                subjectRepository.save(subject);
                parent.getChildren().add(subject);
            }
            // 추가된 자식이 없다면 divide 상태가 되지 않은 것으로 간주
            if (!parent.getChildren().isEmpty()) {
                return;
            }
            parent.setDivide(true);
            subjectRepository.save(parent);
        }
    }

    // 자식들은 cascade로 삭제되므로 부모만 삭제
    void deleteSubject(Long id) {
        //TODO : delete subject
        if (subjectRepository.existsById(id))
            subjectRepository.deleteById(id);
    }

    Optional<Subject> getSubject(Long id) {
        return subjectRepository.findById(id);
    }
}