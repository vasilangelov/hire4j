package com.github.vasilangelov.hire4j.repository;

import com.github.vasilangelov.hire4j.dto.AdminResponse;
import com.github.vasilangelov.hire4j.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<AdminResponse> findAllByRoleName(String roleName);

}
