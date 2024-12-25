package com.github.vasilangelov.hire4j.repository;

import com.github.vasilangelov.hire4j.dto.JobListingDetailsView;
import com.github.vasilangelov.hire4j.model.JobListing;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JobListingRepository extends CrudRepository<JobListing, Long> {

    Optional<JobListingDetailsView> findDetailsViewById(long id);

}
