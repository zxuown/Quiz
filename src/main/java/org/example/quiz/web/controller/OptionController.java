package org.example.quiz.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.aspect.Loggable;
import org.example.quiz.domain.entity.FIleUpload;
import org.example.quiz.domain.entity.Option;
import org.example.quiz.domain.entity.Quiz;
import org.example.quiz.domain.service.OptionService;
import org.example.quiz.domain.service.QuestionService;
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
public class OptionController {

    private final OptionService optionService;

    private final QuestionService questionService;

    private final UserServiceImpl userService;

    @GetMapping("/option/question-options")
    public String getOptions(Model model, @RequestParam int id) {
        model.addAttribute("options", optionService.findAll(id).stream()
                .filter(x-> Objects.equals(x.getUser().getId(), userService.loadUser().getId())));
        model.addAttribute("question", questionService.findById(id));
        return "option/options";
    }

    @GetMapping("/option/add-option")
    public String add(Model model, @RequestParam int id) {
        Option option = new Option();
        option.setQuestion(questionService.findById(id));
        option.setUser(userService.loadUser());
        model.addAttribute("option", option);
        return "option/addOption";
    }

    @PostMapping("/option/add-option")
    @Validated(OnCreate.class)
    public String add(@ModelAttribute @Valid Option option, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        option.setImgUrl(FIleUpload.saveImg(multipartFile));
        optionService.add(option);
        return "redirect:/option/question-options?id=" + optionService.findById(option.getId()).getQuestion().getId();
    }

    @GetMapping("/option/edit-option")
    public String edit(Model model, @RequestParam int id) {
        model.addAttribute("option", optionService.findById(id));
        return "option/editOption";
    }

    @PostMapping("/option/edit-option")
    @Validated(OnUpdate.class)
    public String edit(@ModelAttribute @Valid Option option, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()){
            option.setImgUrl(FIleUpload.saveImg(multipartFile));
        }
        optionService.update(option);
        return "redirect:/option/question-options?id=" + optionService.findById(option.getId()).getQuestion().getId();
    }

    @PostMapping("/option/delete-option")
    public String delete(@RequestParam int id) {
        Option option = optionService.findById(id);
        optionService.delete(option.getId());
        return "redirect:/option/question-options?id=" + option.getQuestion().getId();
    }
}
