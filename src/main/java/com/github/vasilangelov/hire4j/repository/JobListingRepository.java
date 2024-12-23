package com.github.vasilangelov.hire4j.repository;

import com.github.vasilangelov.hire4j.model.JobListing;
import org.springframework.data.repository.CrudRepository;

public interface JobListingRepository extends CrudRepository<JobListing, Long> {

}
