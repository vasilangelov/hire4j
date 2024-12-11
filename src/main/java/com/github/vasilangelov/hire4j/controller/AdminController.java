package com.github.vasilangelov.hire4j.controller;

import com.github.vasilangelov.hire4j.dto.CreateUserRequest;
import com.github.vasilangelov.hire4j.dto.UserView;
import com.github.vasilangelov.hire4j.model.Role;
import com.github.vasilangelov.hire4j.repository.UserRepository;
import com.github.vasilangelov.hire4j.service.UserService;
import com.github.vasilangelov.hire4j.util.BindingResultUtils;
import com.github.vasilangelov.hire4j.util.controller.AllowAdmin;
import com.github.vasilangelov.hire4j.util.controller.AllowSuperAdmin;
import com.github.vasilangelov.hire4j.util.service.ServiceError;
import com.github.vasilangelov.hire4j.util.service.ServiceResult;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @AllowAdmin
    @GetMapping("/admin/users")
    public String users(
            @RequestParam(name = "search", defaultValue = "") String email,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        Page<UserView> users = this.userRepository.findAllByEmailStartingWith(email, PageRequest.of(Math.max(page - 1, 0), 10, Sort.by("email").ascending()));

        model.addAttribute("users", users);

        return "admin/users";
    }

    @AllowSuperAdmin
    @PostMapping("/admin/delete-user")
    public String deleteUser(String email, RedirectAttributes redirectAttributes) {
        ServiceResult deleteUserResult = this.userService.deleteUserByEmail(email);

        if (!deleteUserResult.isSuccess()) {
            List<String> errors = deleteUserResult.getErrors().stream().map(ServiceError::getMessage).toList();

            redirectAttributes.addFlashAttribute("errors", errors);

            return "redirect:/admin/users";
        }

        redirectAttributes.addFlashAttribute("info", "User deleted successfully.");

        return "redirect:/admin/users";
    }

    @AllowSuperAdmin
    @GetMapping("/admin/create-admin")
    public String createAdmin(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());

        return "admin/create-admin";
    }

    @AllowSuperAdmin
    @PostMapping("/admin/create-admin")
    public String createAdmin(@Valid @ModelAttribute("createUserRequest") CreateUserRequest createUserRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/admin/create-admin";
        }

        ServiceResult createAdminServiceResult = this.userService.createUser(createUserRequest, Role.ADMIN);

        if (!createAdminServiceResult.isSuccess()) {
            BindingResultUtils.addResultErrorsToBindingResult(bindingResult, createAdminServiceResult);

            return "/admin/create-admin";
        }

        redirectAttributes.addFlashAttribute("info", "Admin created successfully.");

        return "redirect:/admin/users";
    }

}
