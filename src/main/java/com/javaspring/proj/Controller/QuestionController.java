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

    //получить все вопросы
    @GetMapping(value = "/get_questions")
    public Iterable<Question> getQuestions(){
        return questionRepository.findAll();
    }

    //получить вопросы определенного опроса
    @GetMapping(value = "/get_questions/{poll_name}")
    public List<Question> getQuestList(@PathVariable("poll_name") String name){
        Poll poll = pollRepository.findByName(name);
        return poll.getQuestionList();
    }

    //создать вопрос
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
