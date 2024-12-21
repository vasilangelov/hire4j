package com.github.vasilangelov.hire4j.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany
    @JoinColumn(name = "organization_id")
    private Set<User> maintainers;

    public Organization() {}

    public Organization(String name, String description, Set<User> maintainers) {
        this.name = name;
        this.description = description;
        this.maintainers = maintainers;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getMaintainers() {
        return this.maintainers;
    }

    public void setMaintainers(Set<User> maintainers) {
        this.maintainers = maintainers;
    }

}
