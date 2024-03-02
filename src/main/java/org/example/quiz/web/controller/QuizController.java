package org.example.quiz.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.aspect.Loggable;
import org.example.quiz.domain.entity.FIleUpload;
import org.example.quiz.domain.entity.Question;
import org.example.quiz.domain.entity.Quiz;
import org.example.quiz.domain.service.OptionService;
import org.example.quiz.domain.service.QuestionService;
import org.example.quiz.domain.service.QuizService;
import org.example.quiz.domain.service.impl.UserServiceImpl;
import org.example.quiz.domain.validation.OnCreate;
import org.example.quiz.domain.validation.OnUpdate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Validated
@Loggable
public class QuizController {

    private final QuizService quizService;

    private final UserServiceImpl userService;

    private final QuestionService questionService;

    private final OptionService optionService;


    @GetMapping("/")
    public String getAllQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.findAll().stream()
                .filter(x-> !questionService.findAll(x.getId()).isEmpty() &&
                 questionService.findAll(x.getId())
                .stream().anyMatch(a-> !optionService.findAll(a.getId()).isEmpty())));
        return "quiz/quizzes";
    }

    @GetMapping("/quiz/your-quizzes")
    public String getCurrentUserQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.findAll().stream()
                .filter(x-> Objects.equals(x.getUser().getId(), userService.loadUser().getId())));
        return "quiz/currentUserQuizzes";
    }

    @GetMapping("/quiz/add-quiz")
    public String addQuizGet(Model model) {
        Quiz quiz = new Quiz();
        quiz.setUser(userService.loadUser());
        model.addAttribute("quiz", quiz);
        return "quiz/addQuiz";
    }

    @PostMapping("/quiz/add-quiz")
    @Validated(OnCreate.class)
    public String addQuizPost(@ModelAttribute @Valid Quiz quiz, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        quiz.setImgUrl(FIleUpload.saveImg(multipartFile));
        quizService.add(quiz);
        return "redirect:/quiz/your-quizzes";
    }

    @GetMapping("/quiz/edit-quiz")
    public String editQuizGet(Model model, @RequestParam int id) {
        model.addAttribute("quiz", quizService.findById(id));
        return "quiz/editQuiz";
    }

    @PostMapping("/quiz/edit-quiz")
    @Validated(OnUpdate.class)
    public String editQuizPost(@ModelAttribute @Valid Quiz quiz, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()){
            quiz.setImgUrl(FIleUpload.saveImg(multipartFile));
        }
        quizService.update(quiz.getId(), quiz);
        return "redirect:/quiz/your-quizzes";
    }

    @PostMapping("/quiz/delete-quiz")
    public String deleteQuizPost(@RequestParam int id) {
        quizService.delete(id);
        return "redirect:/quiz/your-quizzes";
    }

}
