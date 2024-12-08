package com.github.vasilangelov.hire4j.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ThymeleafUtils {

    @ModelAttribute("requestURI")
    public String addRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

}
