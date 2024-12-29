package com.github.vasilangelov.hire4j.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private JobApplication jobApplication;

}
