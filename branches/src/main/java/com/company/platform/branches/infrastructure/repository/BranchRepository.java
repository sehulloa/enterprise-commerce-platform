package com.company.platform.branches.infrastructure.repository;

import com.company.platform.branches.domain.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByCode(String code);

    boolean existsByCode(String code);
}
