package org.example.quiz.domain.service;

import org.example.quiz.domain.entity.Option;
import org.example.quiz.domain.entity.Question;

import java.util.List;

public interface OptionService {

    List<Option> findAll();
    List<Option> findAll(int questionId);
    Option findById(int id);

    Option add(Option option);

    Option update(Option option);

    Boolean delete(int id);
}
