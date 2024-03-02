package org.example.quiz.domain.service.impl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.entity.Question;
import org.example.quiz.domain.entity.Quiz;
import org.example.quiz.domain.repository.QuestionRepository;
import org.example.quiz.domain.repository.QuizRepository;
import org.example.quiz.domain.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> findAll(int quizId) {
        return questionRepository.findAll().stream().filter(x->x.getQuiz().getId() == quizId).toList();
    }

    @Override
    public Question findById(int id) {
        return questionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find question with id: " + id));
    }

    @Override
    public Question add(Question question) {
        question.setId(0);
        return questionRepository.save(question);
    }

    @Override
    public Question update(int id, Question question) {
        Question newQuestion = questionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find question with id: " + id));
        newQuestion.setImgUrl(question.getImgUrl());
        newQuestion.setTitle(question.getTitle());
        return questionRepository.save(newQuestion);
    }

    @Override
    public Boolean delete(int id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find question with id: " + id));
        questionRepository.delete(question);
        return true;
    }
}
