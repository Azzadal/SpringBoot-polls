package com.javaspring.proj.Controller;


import com.javaspring.proj.Model.Poll;
import com.javaspring.proj.Model.Question;
import com.javaspring.proj.Repository.PollRepository;
import com.javaspring.proj.Repository.QuestionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class QuestionController {
    private final QuestionRepository questionRepository;
    private final PollRepository pollRepository;

    public QuestionController(QuestionRepository questionRepository, PollRepository pollRepository){
        this.questionRepository = questionRepository;
        this.pollRepository = pollRepository;
    }

    @GetMapping(value = "/get_questions")
    public Iterable<Question> getQuestions(){
        return questionRepository.findAll();
    }

    @PostMapping(value = "/api/add_question")
    public void addQuestion(@RequestBody Object obj){
        Map<String, String> lhm = (LinkedHashMap<String, String>) obj;
        try {
            Poll poll = pollRepository.findByName(lhm.get("pollName"));
            String content = lhm.get("content");
            String displayOrder = lhm.get("displayOrder");
            Question question = new Question(poll, content, displayOrder);
            questionRepository.save(question);
        } catch (Exception e){
            System.out.println(e);
        }

    }
}
