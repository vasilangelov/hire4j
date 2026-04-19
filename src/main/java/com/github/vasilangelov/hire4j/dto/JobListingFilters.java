package com.github.vasilangelov.hire4j.dto;

import java.util.Collection;
import java.util.Objects;

public class JobListingFilters {

    private String search = "";

    private Collection<String> tags;

    private String location = "";

    private Byte minYearsOfExperience;

    private int page = 1;

    public String getSearch() {
        return this.search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Collection<String> getTags() {
        return this.tags;
    }

    public void setTags(Collection<String> tags) {
        if (tags == null) {
            this.tags = null;
            return;
        }

        var sanitized = tags.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(tag -> !tag.isBlank())
            .distinct()
            .toList();

        this.tags = sanitized.isEmpty() ? null : sanitized;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Byte getMinYearsOfExperience() {
        return this.minYearsOfExperience;
    }

    public void setMinYearsOfExperience(Byte minYearsOfExperience) {
        this.minYearsOfExperience = minYearsOfExperience;
    }

}
