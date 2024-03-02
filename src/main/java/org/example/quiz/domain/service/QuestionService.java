package org.example.quiz.domain.service;

import org.example.quiz.domain.entity.Question;
import org.example.quiz.domain.entity.Quiz;
import org.example.quiz.domain.repository.QuestionRepository;

import java.util.List;

public interface QuestionService {

    List<Question> findAll(int quizId);

    Question findById(int id);

    Question add(Question question);

    Question update(int id, Question question);

    Boolean delete(int id);
}
