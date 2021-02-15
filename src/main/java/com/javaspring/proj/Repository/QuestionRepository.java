package com.javaspring.proj.Repository;

import com.javaspring.proj.Model.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
}
