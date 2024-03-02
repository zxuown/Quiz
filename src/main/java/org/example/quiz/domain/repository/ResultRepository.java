package org.example.quiz.domain.repository;

import org.example.quiz.domain.entity.Quiz;
import org.example.quiz.domain.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
}
