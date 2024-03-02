package org.example.quiz.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.entity.Quiz;
import org.example.quiz.domain.repository.QuizRepository;
import org.example.quiz.domain.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    @Override
    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz findById(int id) {
        return quizRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find quiz with id: " + id));
    }

    @Override
    public Quiz add(Quiz quiz) {
        quiz.setId(0);
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz update(int id, Quiz quiz) {
        Quiz newQuiz = quizRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find quiz with id: " + id));
        newQuiz.setImgUrl(quiz.getImgUrl());
        newQuiz.setTitle(quiz.getTitle());
        newQuiz.setDescription(quiz.getDescription());
        newQuiz.setActive(quiz.isActive());
        return quizRepository.save(newQuiz);
    }

    @Override
    public boolean delete(int id) {
        Quiz newQuiz = quizRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find quiz with id: " + id));
        quizRepository.delete(newQuiz);
        return true;
    }
}
