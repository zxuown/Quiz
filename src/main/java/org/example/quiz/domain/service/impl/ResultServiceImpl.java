package org.example.quiz.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.entity.Result;
import org.example.quiz.domain.repository.ResultRepository;
import org.example.quiz.domain.service.ResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    @Override
    public List<Result> findAll() {
        return resultRepository.findAll();
    }

    @Override
    public Result findById(int id) {
        return resultRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find result with id: " + id));
    }

    @Override
    public Result add(Result result) {
        result.setId(0);
        return resultRepository.save(result);
    }
}
