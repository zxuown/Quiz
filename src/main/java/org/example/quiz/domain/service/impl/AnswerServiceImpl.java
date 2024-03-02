package org.example.quiz.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.entity.Answer;
import org.example.quiz.domain.repository.AnswerRepository;
import org.example.quiz.domain.service.AnswerService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;


    @Override
    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    @Override
    public Answer add(Answer answer) {
        answer.setId(0);
        return answerRepository.save(answer);
    }
}
