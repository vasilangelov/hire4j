package com.github.vasilangelov.hire4j.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;

public interface JobListingDetailsView {

    Long getId();

    @Value("#{target.organization.id}")
    Long getOrganizationId();

    String getTitle();

    String getDescription();

    Byte getMinYearsOfExperience();

    String getLocation();

    @Value("#{target.tags.![displayName]}")
    Collection<String> getTags();

    default Collection<String> getSortedTags() {
        return this.getTags().stream().sorted().toList();
    }

}
