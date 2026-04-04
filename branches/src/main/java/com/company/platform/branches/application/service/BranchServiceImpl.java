package com.company.platform.branches.application.service;

import com.company.platform.branches.api.dto.BranchResponse;
import com.company.platform.branches.api.dto.CreateBranchRequest;
import com.company.platform.branches.api.dto.UpdateBranchStatusRequest;
import com.company.platform.branches.domain.enumtype.BranchStatus;
import com.company.platform.branches.domain.model.Branch;
import com.company.platform.branches.infrastructure.repository.BranchRepository;
import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.common.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    public BranchResponse createBranch(CreateBranchRequest request) {

        log.info("Creating branch with code={}", request.getCode());

        String normalizedCode = request.getCode().trim().toUpperCase();

        if (branchRepository.existsByCode(normalizedCode)) {
            throw new BusinessException("Branch already exists with code: " + normalizedCode);
        }

        Branch branch = new Branch();
        branch.setCode(normalizedCode);
        branch.setName(request.getName().trim());
        branch.setAddress(request.getAddress() != null ? request.getAddress().trim() : null);
        branch.setStatus(BranchStatus.ACTIVE);

        Branch saved = branchRepository.save(branch);

        log.info("Branch created successfully with branchId={} code={}",
                saved.getId(),
                saved.getCode());

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponse getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + id));

        return mapToResponse(branch);

    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public BranchResponse updateBranchStatus(Long id, UpdateBranchStatusRequest request) {

        log.info("Updating branch status for branchId={} newStatus={}",
                id,
                request.getStatus());

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + id));

        validateStatusChange(branch, request.getStatus());

        branch.setStatus(request.getStatus());

        Branch updated = branchRepository.save(branch);

        log.info("Branch status updated successfully for branchId={} status={}",
                updated.getId(),
                updated.getStatus());

        return mapToResponse(updated);
    }

    private void validateStatusChange(Branch branch, BranchStatus newStatus) {
        if (branch.getStatus() == newStatus) {
            throw new BusinessException("Branch already has status: " + newStatus);
        }

    }

    private BranchResponse mapToResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .code(branch.getCode())
                .name(branch.getName())
                .address(branch.getAddress())
                .status(branch.getStatus())
                .build();
    }

}
