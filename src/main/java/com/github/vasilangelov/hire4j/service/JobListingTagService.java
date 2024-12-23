package com.github.vasilangelov.hire4j.service;

import com.github.vasilangelov.hire4j.model.JobListingTag;
import com.github.vasilangelov.hire4j.repository.JobListingTagRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class JobListingTagService {

    private final JobListingTagRepository jobListingTagRepository;

    public JobListingTagService(JobListingTagRepository jobListingTagRepository) {
        this.jobListingTagRepository = jobListingTagRepository;
    }

    public Set<JobListingTag> upsertTags(String[] tagNames) {
        Collection<JobListingTag> tags = this.jobListingTagRepository.findAllByNormalizedNameIn(Set.of(tagNames));
        Collection<JobListingTag> newTags = new HashSet<>();

        for (String tagName : tagNames) {
            if (tags.stream().noneMatch((tag) -> tag.getNormalizedName().equals(tagName))) {
                newTags.add(new JobListingTag(tagName.toLowerCase(), tagName));
            }
        }

        if (!newTags.isEmpty()) {
            this.jobListingTagRepository.saveAll(newTags);
            tags.addAll(newTags);
        }

        return new HashSet<>(tags);
    }

}
