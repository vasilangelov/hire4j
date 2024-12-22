package com.github.vasilangelov.hire4j.controller;

import com.github.vasilangelov.hire4j.dto.EditOrganizationRequest;
import com.github.vasilangelov.hire4j.model.Organization;
import com.github.vasilangelov.hire4j.repository.OrganizationRepository;
import com.github.vasilangelov.hire4j.repository.UserRepository;
import com.github.vasilangelov.hire4j.util.controller.AllowUser;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrganizationController {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public OrganizationController(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    @AllowUser
    @GetMapping("/organization/{id}")
    public String settings(@PathVariable Long id, Model model) {
        if (!this.isInOrganization(id)) {
            return "redirect:/";
        }

        Organization organization = this.organizationRepository.findById(id).orElse(null);

        if (organization == null) {
            return "redirect:/";
        }

        EditOrganizationRequest editOrganizationRequest = new EditOrganizationRequest(organization.getName(), organization.getDescription());

        model.addAttribute("organization", organization);
        model.addAttribute("editOrganizationRequest", editOrganizationRequest);

        return "organization/settings";
    }

    @AllowUser
    @PostMapping("/organization/{id}/edit")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("editOrganizationRequest") EditOrganizationRequest editOrganizationRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (!this.isInOrganization(id)) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            Organization organization = this.organizationRepository.findById(id).orElse(null);

            model.addAttribute("organization", organization);
            model.addAttribute("editOrganizationRequest", editOrganizationRequest);

            return "organization/settings";
        }

        this.organizationRepository
                .findById(id)
                .ifPresent(organization -> {
                    organization.setName(editOrganizationRequest.getName());
                    organization.setDescription(editOrganizationRequest.getDescription());
                    this.organizationRepository.save(organization);
                });

        redirectAttributes.addFlashAttribute("info", "Organization updated successfully.");

        return "redirect:/organization/" + id;
    }

    private boolean isInOrganization(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        String email = authentication.getName();

       return this.userRepository
               .findByEmail(email)
               .map(user -> user.getOrganization().getId().equals(id))
               .orElse(false);
    }

}
