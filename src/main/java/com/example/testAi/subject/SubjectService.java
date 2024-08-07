package com.example.testAi.subject;


import com.example.testAi.chat.ChatService;
import com.example.testAi.user.domain.member.entity.Member;
import com.example.testAi.user.global.rp.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private final Rq request;

    Optional<Subject> get(Long id) {
        return subjectRepository.findById(id);
    }

    List<Subject> getAll() {
        if (request.isLogin()) {
            return subjectRepository.findAllByMemberId(Sort.by(Sort.Order.desc("createdDate")), request.getMember().getId());
        } else {
            return new ArrayList<>();
        }

    }

    List<Subject> getFavorite() {
        if(request.isLogin()) {
            return request.getMember().getFavorite();
        } else {
            return new ArrayList<>();
        }
    }

    void delFavorite(Long id) {
        if (request.isLogin()) {
            Member member = request.getMember();
            Subject subject = get(id).orElse(null);
            if (subject != null) {
                member.getFavorite().remove(subject);
            }
        }
    }

    void addFavorite(Long id) {
        if (request.isLogin()) {
            Member member = request.getMember();
            Subject subject = get(id).orElse(null);
            if (subject != null) {
                member.getFavorite().add(subject);
            }
        }
    }

    // 에러 발생시 빈 리스트 반환
    List<SubjectForm> divide(Long id) {
        //TODO : divide subjects

        Subject subject = get(id).orElse(null);

        if (subject == null || !request.isLogin()) {

            return new ArrayList<>();
        }
        var response = chatService.generate(subject.getSubject());

        Map<String, Object> map = ChatService.jsonConverter(response);
        if (map.get("Error") != null) {
            return new ArrayList<>();
        }

        List<SubjectForm> subjects = new ArrayList<>();

        int no = 1;
        for (Map<String, Object> m : (List<Map<String, Object>>) map.get("subjects")) {
            SubjectForm subjectForm = new SubjectForm();
            subjectForm.setNo(no++);
            subjectForm.setSubject((String) m.getOrDefault("subject", "Nothing"));
            subjectForm.setDescription((String) m.getOrDefault("description", "Nothing"));
            subjectForm.setExpectDate((Integer) m.getOrDefault("expect_date", 0));
            String priority = (String) m.getOrDefault("priority", "0");
            subjectForm.setPriority(Integer.parseInt(priority));
            subjects.add(subjectForm);
        }
        subjects.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return subjects;
    }

    void save(Subject parent, List<SubjectForm> subjectForms) {
        //TODO : save subject
        if (parent == null || !request.isLogin()) {
            return;
        }
        Member member = request.getMember();


        for (SubjectForm subjectForm : subjectForms) {
            Subject subject = new Subject();
            subject.setSubject(subjectForm.getSubject());
            subject.setDescription(subjectForm.getDescription());
            subject.setExpiredDate(LocalDateTime.now().plusDays(subjectForm.getExpectDate()));
            subject.setPriority(subjectForm.getPriority());
            subject.setCreatedDate(LocalDateTime.now());
            subject.setParent(parent);
            subject.setMember(member);

            subjectRepository.save(subject);
            parent.getChildren().add(subject);
        }
        subjectRepository.save(parent);
    }


    // 자식들은 cascade로 삭제되므로 부모만 삭제
    void delete(Long id) {
        //TODO : delete subject
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
        }
    }

    void add(Long id, String content) {
        if (!request.isLogin()) {
            return;
        }


        Subject subject = new Subject();
        subject.setSubject(content);
        subject.setDescription("사용자가 직접 입력한 내용입니다.");
        subject.setCreatedDate(LocalDateTime.now());
        subject.setMember(request.getMember());


        if (id != null && subjectRepository.existsById(id)) {
            Subject parent = subjectRepository.findById(id).orElse(null);
            subject.setParent(parent);
            parent.getChildren().add(subject);
            subjectRepository.save(parent);
        }

        subjectRepository.save(subject);
    }

}