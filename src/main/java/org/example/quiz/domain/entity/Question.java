package org.example.quiz.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id can't be null",groups = OnUpdate.class)
    private int id;

    @ManyToOne
    @NotNull(message = "Quiz can't be null",groups = OnUpdate.class)
    private Quiz quiz;

    @Nullable
    private String imgUrl;

    @NotBlank(message = "Title can't be blank", groups = {OnUpdate.class, OnCreate.class})
    private String title;

    @NotNull(message = "User can't be null", groups = {OnUpdate.class, OnCreate.class})
    @ManyToOne
    private User user;
}
