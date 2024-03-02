package org.example.quiz.domain.service;


import org.example.quiz.domain.entity.Answer;

import java.util.List;

public interface AnswerService {

    List<Answer> findAll();
    Answer add(Answer answer);
}
