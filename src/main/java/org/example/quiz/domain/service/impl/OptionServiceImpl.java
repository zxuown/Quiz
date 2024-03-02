package org.example.quiz.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.entity.Option;
import org.example.quiz.domain.entity.Question;
import org.example.quiz.domain.repository.OptionRepository;
import org.example.quiz.domain.repository.QuestionRepository;
import org.example.quiz.domain.service.OptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Override
    public List<Option> findAll() {
        return optionRepository.findAll();
    }

    @Override
    public List<Option> findAll(int questionId) {
        return optionRepository.findAll().stream().filter(x->x.getQuestion().getId() == questionId).toList();
    }
    @Override
    public Option findById(int id) {
        return optionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find option with id: " + id));
    }

    @Override
    public Option add(Option option) {
        option.setId(0);
        return optionRepository.save(option);
    }

    @Override
    public Option update(Option option) {
        Option newOption = optionRepository.findById(option.getId())
                .orElseThrow(()->new EntityNotFoundException("Cannot find option with id: " + option.getId()));
        newOption.setImgUrl(option.getImgUrl());
        newOption.setTitle(option.getTitle());
        newOption.setCorrect(option.isCorrect());
        return optionRepository.save(option);
    }

    @Override
    public Boolean delete(int id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Cannot find option with id: " + id));
        optionRepository.delete(option);
        return true;
    }
}
