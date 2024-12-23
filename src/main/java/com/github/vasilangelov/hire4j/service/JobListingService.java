package com.github.vasilangelov.hire4j.service;

import com.github.vasilangelov.hire4j.dto.CreateJobListingRequest;
import com.github.vasilangelov.hire4j.model.JobListing;
import com.github.vasilangelov.hire4j.model.JobListingTag;
import com.github.vasilangelov.hire4j.model.Organization;
import com.github.vasilangelov.hire4j.repository.JobListingRepository;
import com.github.vasilangelov.hire4j.repository.OrganizationRepository;
import com.github.vasilangelov.hire4j.util.service.ServiceResult;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class JobListingService {

    private final JobListingRepository jobListingRepository;
    private final OrganizationRepository organizationRepository;
    private final JobListingTagService jobListingTagService;

    public JobListingService(
            JobListingRepository jobListingRepository,
            OrganizationRepository organizationRepository,
            JobListingTagService jobListingTagService) {
        this.jobListingRepository = jobListingRepository;
        this.organizationRepository = organizationRepository;
        this.jobListingTagService = jobListingTagService;
    }

    public ServiceResult createJobListing(long organizationId, CreateJobListingRequest request) {
        Organization organization = this.organizationRepository.findById(organizationId).orElse(null);

        if (organization == null) {
            return ServiceResult.failure("Organization not found");
        }

        Set<JobListingTag> tags = this.jobListingTagService.upsertTags(request.getTags());

        this.jobListingRepository.save(new JobListing(
                request.getTitle(),
                request.getDescription(),
                request.getMinYearsOfExperience(),
                request.getLocation(),
                organization,
                tags
        ));

        return ServiceResult.success();
    }

}
