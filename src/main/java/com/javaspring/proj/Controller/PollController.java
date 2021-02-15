package com.javaspring.proj.Controller;

import org.apache.commons.lang3.StringUtils;
import com.javaspring.proj.Model.Poll;
import com.javaspring.proj.Model.Question;
import com.javaspring.proj.Repository.PollRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class PollController {

    private final PollRepository pollRepository;

    public PollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @GetMapping(value = "/get_polls")
    public Iterable<Poll> getPolls(){
        return pollRepository.findAll();
    }

    @GetMapping(value = "/get_polls/onSubString/{substring}")
    public ArrayList<Poll> getNameOnSubString(@PathVariable ("substring") String substring){
        Iterable<Poll> polls = pollRepository.findAll();
        ArrayList<Poll> pollsAfterFilter = new ArrayList<>();
        polls.forEach(el -> {
            if (StringUtils.containsIgnoreCase(el.getName(), substring))
                pollsAfterFilter.add(pollRepository.findByName(el.getName()));
        });
        return pollsAfterFilter;
    }

    @GetMapping(value = "/get_polls/onDateStart/{date}")
    public Iterable<Poll> getPollsOnDateStart(@PathVariable ("date") String dateStart) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStart);
        return pollRepository.findByDateStart(date);
    }

    @GetMapping(value = "/get_polls/onDateEnd/{date}")
    public Iterable<Poll> getPollsOnDateEnd(@PathVariable ("date") String dateStart) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStart);
        return pollRepository.findByDateEnd(date);
    }

    @GetMapping(value = "/get_questions/{poll_name}")
    public List<Question> getQuestList(@PathVariable ("poll_name") String name){
        Poll poll = pollRepository.findByName(name);
        return poll.getQuestionList();
    }

    @PostMapping(value = "/api/add_poll")
    public Poll addPoll(@RequestBody Poll poll){
        pollRepository.save(poll);
        return pollRepository.findByName(poll.getName());
    }

    @PutMapping("api/edit_poll/{name}")
    public Poll update(@PathVariable ("name") String name, @RequestBody Poll poll){
        Poll dbPoll = pollRepository.findByName(name);
        BeanUtils.copyProperties(poll, dbPoll, "id", "questionList");
        return pollRepository.save(dbPoll);
    }

    @DeleteMapping("delete_poll/{poll_name}")
    public void delete(@PathVariable ("poll_name") String name){
        Poll poll = pollRepository.findByName(name);
        pollRepository.delete(poll);
    }
}
