package com.example.testAi.chat;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiChatModel chatModel;
    private final static String CONFIG =  """
               '>'를 기준으로 왼쪽은 상위 주제, 오른쪽은 하위 주제입니다.
               최하위 주제(화살표 가장 오른쪽)을 달성하기 위해 필요한 주제들을 추천해드립니다.
                최소 5개 최대 10개의 주제를 포함해야 합니다.
                모든 내용은 JSON 형식으로 subjects 태그 안에 배열로 담아주세요.
                태그는 subject, description, expect_date, priority로 구성되어 있습니다.
                subject의 내용은 한국어로 반환해 주세요.
                description은 주제에 대한 짧은 한 문장의 설명을 한국어로 반환해주세요
                expect_date는 예상 필요 기간을 일 단위 숫자로 반환해주세요.
                priority는 1부터 10까지의 정수 형태로 반환해주세요. 1이 가장 중요하고 10이 가장 낮은 우선순위입니다.
                예시: {"subjects":[{"subject":"요리사 자격증 취득","description":"요리사 자격증 취득을 위한 과정","expect_date":30,"priority":"1"}]}
               """;


    public String generate(String message) {

        try {
            List<Message> request = new ArrayList<>();
            request.add(new CustomMessage(CONFIG, MessageType.ASSISTANT));
            request.add(new CustomMessage(message, MessageType.USER));
            var OpenAiResponse = chatModel.call(new Prompt(request));
            return OpenAiResponse.getResult().getOutput().getContent();

        } catch (Exception e) {
            String error = e.getMessage();
            e.printStackTrace();
            return "{Error : OpenAI Setting Error}";
        }
    }

    public static Map<String, Object> jsonConverter(String json) {
        System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);

        if (jsonObject.has("subjects") && jsonObject.get("subjects") instanceof JSONArray) {
            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                map.put("subject", object.get("subject"));
                map.put("description", object.get("description"));
                map.put("expect_date", object.get("expect_date"));
                map.put("priority", object.get("priority"));
                list.add(map);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("subjects", list);
            return result;
        } else if (jsonObject.has("Error")) {
            return Map.of("Error", jsonObject.get("Error"));
        } else {
            return Map.of("Error", "Unknown Error");
        }
    }
}