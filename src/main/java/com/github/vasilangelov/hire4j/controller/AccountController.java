package com.github.vasilangelov.hire4j.controller;

import com.github.vasilangelov.hire4j.dto.CreateUserRequest;
import com.github.vasilangelov.hire4j.model.Role;
import com.github.vasilangelov.hire4j.service.UserService;
import com.github.vasilangelov.hire4j.util.BindingResultUtils;
import com.github.vasilangelov.hire4j.util.service.ServiceResult;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account/sign-in")
    public String signIn(Model model) {
        return "account/sign-in";
    }

    @GetMapping("/account/sign-up")
    public String signUp(Model model) {
        model.addAttribute("signUpRequest", new CreateUserRequest());

        return "account/sign-up";
    }

    @PostMapping("/account/sign-up")
    public String signUp(@Valid @ModelAttribute("signUpRequest") CreateUserRequest signUpRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "account/sign-up";
        }

        ServiceResult signUpServiceResult = this.userService.createUser(signUpRequest, Role.USER);

        if (!signUpServiceResult.isSuccess()) {
            BindingResultUtils.addResultErrorsToBindingResult(bindingResult, signUpServiceResult);

            return "account/sign-up";
        }

        return "redirect:/account/sign-in";
    }

}
