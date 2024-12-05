package com.github.vasilangelov.hire4j.controller;

import com.github.vasilangelov.hire4j.dto.CreateUserRequest;
import com.github.vasilangelov.hire4j.model.Role;
import com.github.vasilangelov.hire4j.service.UserService;
import com.github.vasilangelov.hire4j.util.BindingResultUtils;
import com.github.vasilangelov.hire4j.util.controller.AllowSuperAdmin;
import com.github.vasilangelov.hire4j.util.service.ServiceError;
import com.github.vasilangelov.hire4j.util.service.ServiceResult;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @AllowSuperAdmin
    @GetMapping("/admin/all")
    public String showAllAdmins(Model model) {
        model.addAttribute("admins", this.userService.getAdmins());

        return "admin/all";
    }

    @AllowSuperAdmin
    @PostMapping("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ServiceResult result = this.userService.deleteUser(id);

        if (!result.isSuccess()) {
            Optional<ServiceError> serviceError = result.getErrors().stream().findFirst();

            serviceError.ifPresent(error -> redirectAttributes.addFlashAttribute("message", error.getMessage()));
        }

        return "redirect:/admin/all";
    }

    @AllowSuperAdmin
    @GetMapping("/admin/create")
    public String createAdmin(Model model) {
        model.addAttribute("admin", new CreateUserRequest());

        return "admin/create";
    }

    @AllowSuperAdmin
    @PostMapping("/admin/create")
    public String createAdmin(@Valid @ModelAttribute("createAdminRequest")CreateUserRequest createUserRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/create";
        }

        ServiceResult createAdminServiceResult = this.userService.createUser(createUserRequest, Role.ADMIN);

        if (!createAdminServiceResult.isSuccess()) {
            BindingResultUtils.addResultErrorsToBindingResult(bindingResult, createAdminServiceResult);

            return "admin/create";
        }

        return "redirect:/admin/all";
    }

}
