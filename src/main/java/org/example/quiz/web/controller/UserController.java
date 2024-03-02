package org.example.quiz.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.quiz.domain.aspect.Loggable;
import org.example.quiz.domain.entity.User;
import org.example.quiz.domain.service.impl.UserServiceImpl;
import org.example.quiz.web.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Validated
@Loggable
public class UserController {

    private final UserServiceImpl userService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/registration")
    public String getRegistration(Model model){
        model.addAttribute("loginRequest",  new LoginRequest());
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(LoginRequest loginRequest){
        if (userService.findByGmail(loginRequest.getEmail()) == null){
            User user = new User();
            user.setUsername(loginRequest.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(loginRequest.getPassword()));
            userService.add(user);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }

    @GetMapping("/custom-login")
    public String getLogin(Model model){
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }
}
