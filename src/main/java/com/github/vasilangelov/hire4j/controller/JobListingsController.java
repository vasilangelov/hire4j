package com.github.vasilangelov.hire4j.controller;

import com.github.vasilangelov.hire4j.dto.JobListingDetailsView;
import com.github.vasilangelov.hire4j.dto.JobListingFilters;
import com.github.vasilangelov.hire4j.dto.JobListingFiltersView;
import com.github.vasilangelov.hire4j.service.JobListingService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;

@Controller
public class JobListingsController {

    private final JobListingService jobListingService;

    public JobListingsController(JobListingService jobListingService) {
        this.jobListingService = jobListingService;
    }

    @GetMapping("/job-listings")
    public String index(
            @ModelAttribute JobListingFilters filters,
            Model model
    ) {
        JobListingFiltersView filtersView = this.jobListingService.getJobListingFilters(filters);

        Page<JobListingDetailsView> page = this.jobListingService.searchJobListings(filters);

        model.addAttribute("activeTagsSet", new HashSet<>(filters.getTags() != null ? filters.getTags() : List.of()));
        model.addAttribute("filters", filtersView);
        model.addAttribute("page", page);

        return "job-listings/index";
    }

    @GetMapping("/job-listings/{id}")
    public String details(@PathVariable Long id, Model model) {
        JobListingDetailsView jobListing = this.jobListingService.findJobListingDetailsView(id);

        if (jobListing == null) {
            return "redirect:/job-listings";
        }

        model.addAttribute("jobListing", jobListing);

        return "job-listings/details";
    }

}
