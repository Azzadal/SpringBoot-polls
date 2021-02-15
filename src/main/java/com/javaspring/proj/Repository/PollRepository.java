package com.javaspring.proj.Repository;

import com.javaspring.proj.Model.Poll;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface PollRepository extends CrudRepository<Poll, Integer> {
    Poll findByName(String name);
    Iterable<Poll> findByDateStart(Date dateStart);
    Iterable<Poll> findByDateEnd(Date dateEnd);
}
