package org.example.quiz.domain.service;

import org.example.quiz.domain.entity.Quiz;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuizService {
    List<Quiz> findAll();

    Quiz findById(int id);

    Quiz add(Quiz quiz);

    Quiz update(int id, Quiz quiz);

    boolean delete(int id);
}
