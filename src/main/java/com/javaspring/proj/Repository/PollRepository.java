package com.javaspring.proj.Repository;

import com.javaspring.proj.Model.Poll;
import com.javaspring.proj.Model.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PollRepository extends CrudRepository<Poll, Integer> {
    Poll findByName(String name);
    Iterable<Poll> findByDateStart(Date dateStart);
    Iterable<Poll> findByDateEnd(Date dateEnd);
    Iterable<Poll> findByNameOrderByDateStartDesc(String name);
}
