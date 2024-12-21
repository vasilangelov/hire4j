package com.github.vasilangelov.hire4j.repository;

import com.github.vasilangelov.hire4j.dto.OrganizationView;
import com.github.vasilangelov.hire4j.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    Page<OrganizationView> findByNameStartingWithIgnoreCase(String organization, Pageable pageable);

}
