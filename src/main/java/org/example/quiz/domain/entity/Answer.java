package org.example.quiz.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.example.quiz.domain.validation.OnCreate;
import org.example.quiz.domain.validation.OnUpdate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id can't be null", groups = OnUpdate.class)
    private int id;

    @NotNull(message = "Option can't be null", groups = {OnUpdate.class, OnCreate.class})
    @ManyToOne
    private Option option;

    @NotNull(message = "Result can't be null", groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    @ManyToOne
    private Result result;

    @NotNull(message = "User can't be null", groups = {OnUpdate.class, OnCreate.class})
    @ManyToOne
    private User user;
}
