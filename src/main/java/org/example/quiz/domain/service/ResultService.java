package org.example.quiz.domain.service;

import org.example.quiz.domain.entity.Result;

import java.util.List;

public interface ResultService {
    List<Result> findAll();

    Result findById(int id);

    Result add(Result result);
}
