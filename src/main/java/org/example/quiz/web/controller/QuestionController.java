package org.example.quiz.web.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.aspect.Loggable;
import org.example.quiz.domain.entity.*;
import org.example.quiz.domain.service.*;
import org.example.quiz.domain.service.impl.UserServiceImpl;
import org.example.quiz.domain.validation.OnCreate;
import org.example.quiz.domain.validation.OnUpdate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
@RequiredArgsConstructor
@Validated
@Loggable
public class QuestionController {

    private final QuestionService questionService;

    private final QuizService quizService;

    private final OptionService optionService;

    private final AnswerService answerService;

    private final ResultService resultService;

    private final UserServiceImpl userService;

    @GetMapping("/question/quiz-questions")
    public String getQuestions(Model model, @RequestParam int id) {
        model.addAttribute("questions", questionService.findAll(id).stream()
                .filter(x -> Objects.equals(x.getUser().getId(), userService.loadUser().getId())));
        model.addAttribute("quiz", quizService.findById(id));
        return "question/questions";
    }

    @GetMapping("/question/add-question")
    public String add(Model model, @RequestParam int id) {
        Question question = new Question();
        question.setUser(userService.loadUser());
        question.setQuiz(quizService.findById(id));
        model.addAttribute("question", question);
        return "question/addQuestion";
    }

    @PostMapping("/question/add-question")
    @Validated(OnCreate.class)
    public String add(@ModelAttribute @Valid Question question, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        question.setImgUrl(FIleUpload.saveImg(multipartFile));
        questionService.add(question);
        return "redirect:/question/quiz-questions?id=" + question.getQuiz().getId();
    }

    @GetMapping("/question/edit-question")
    public String update(Model model, @RequestParam int id) {
        model.addAttribute("question", questionService.findById(id));
        return "question/editQuestion";
    }

    @PostMapping("/question/edit-question")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute @Valid Question question, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()){
            question.setImgUrl(FIleUpload.saveImg(multipartFile));
        }
        questionService.update(question.getId(), question);
        return "redirect:/question/quiz-questions?id=" + question.getQuiz().getId();
    }

    @PostMapping("/question/delete-question")
    public String delete(@RequestParam int id) {
        Question question = questionService.findById(id);
        questionService.delete(question.getId());
        return "redirect:/question/quiz-questions?id=" + question.getQuiz().getId();
    }

    private int questionCount = 0;
    private List<Integer> optionsId = new ArrayList<>();

    private boolean equalsUsers;

    @PostMapping("/api/user")
    public boolean createUser(@RequestBody RequestUserEmail request) {
        return equalsUsers = userService.loadUser().getUsername().equals(request.getPrevEmail());
    }

    @GetMapping("/question/start")
    public String startQuiz(Model model, HttpSession session, @RequestParam int id) {
        Integer quizIdInSession = (Integer) session.getAttribute("quizId");
        if (quizIdInSession != null && quizIdInSession != id || !equalsUsers) {
            questionCount = 0;
            optionsId = new ArrayList<>();
            equalsUsers = true;
        }
        session.setAttribute("quizId", id);
        Question currentQuestion = questionService.findAll(id).get(questionCount);
        model.addAttribute("question", questionService.findAll(id).get(questionCount));
        model.addAttribute("questions", questionService.findAll(id));
        model.addAttribute("options", optionService.findAll(currentQuestion.getId()));
        return "question/currentUserQuestions";
    }

    @PostMapping("/question/nextQuestion")
    public String handleNextQuestion(@RequestParam int id) throws Exception {
        Option option = optionService.findById(id);
        int quizId = option.getQuestion().getQuiz().getId();
        int countQuestions = questionService.findAll(option.getQuestion().getQuiz().getId()).size();
        if (questionCount <= countQuestions) {
            optionsId.add(id);
            questionCount++;
        }
        if (countQuestions == questionCount) {
            questionCount = 0;
            Result result = new Result();
            result.setUser(userService.loadUser());
            Result resultFromBd = resultService.add(result);
            for (int optionId : optionsId) {
                Answer answer = new Answer();
                answer.setOption(optionService.findById(optionId));
                answer.setResult(resultFromBd);
                answer.setUser(userService.loadUser());
                answerService.add(answer);
            }
            optionsId = new ArrayList<>();
            return "redirect:/result/" + resultFromBd.getId() + "/" + quizId;
        }
        return "redirect:/question/start?id=" + quizId;
    }

    @GetMapping("/result/{resultId}/{quizId}")
    public String result(Model model, @PathVariable int resultId, @PathVariable int quizId) {
        List<Question> questions = questionService.findAll(quizId);
        List<Option> options = optionService.findAll().stream()
                .filter(x -> questions.stream().anyMatch(a -> a.getId() == x.getQuestion().getId())).toList();
        List<Answer> answers = answerService.findAll().stream().filter(x -> x.getResult().getId() == resultId).toList();
        model.addAttribute("quiz", quizService.findById(quizId));
        model.addAttribute("questions", questions);
        model.addAttribute("options", options);
        model.addAttribute("answers", answers);
        model.addAttribute("correctAnswers", answers.stream().filter(x -> x.getOption().isCorrect()));
        return "result";
    }
}
