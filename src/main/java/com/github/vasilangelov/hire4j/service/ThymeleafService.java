package com.github.vasilangelov.hire4j.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ThymeleafService {

    private final HttpServletRequest request;
    private final RoleHierarchy roleHierarchy;

    public ThymeleafService(HttpServletRequest request, RoleHierarchy roleHierarchy) {
        this.request = request;
        this.roleHierarchy = roleHierarchy;
    }

    public String getRequestURI() {
        return this.request.getRequestURI();
    }

    public boolean isInRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        return this.roleHierarchy
                .getReachableGrantedAuthorities(authentication.getAuthorities())
                .contains(new SimpleGrantedAuthority(roleName));
    }

}
