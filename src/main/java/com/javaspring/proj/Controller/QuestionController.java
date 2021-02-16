package com.javaspring.proj.Controller;


import com.javaspring.proj.Model.Poll;
import com.javaspring.proj.Model.Question;
import com.javaspring.proj.Repository.PollRepository;
import com.javaspring.proj.Repository.QuestionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionController {
    private final QuestionRepository questionRepository;
    private final PollRepository pollRepository;

    public QuestionController(QuestionRepository questionRepository, PollRepository pollRepository){
        this.questionRepository = questionRepository;
        this.pollRepository = pollRepository;
    }

    @GetMapping(value = "/questions")
    public Iterable<Question> getQuestions(){
        return questionRepository.findAll();
    }

    @GetMapping(value = "/questions/{poll_name}")
    public List<Question> getQuestList(@PathVariable("poll_name") String name){
        Poll poll = pollRepository.findByName(name);
        return poll.getQuestionList();
    }

    @PostMapping(value = "/api/add_question")
    public void addQuestion(@RequestBody LinkedHashMap<String, String> obj){
        try {
            Poll poll = pollRepository.findByName(obj.get("pollName"));
            String content = obj.get("content");
            String displayOrder = obj.get("displayOrder");
            Question question = new Question(poll, content, displayOrder);
            questionRepository.save(question);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
