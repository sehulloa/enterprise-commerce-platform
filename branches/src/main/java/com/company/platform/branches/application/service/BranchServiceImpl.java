package com.company.platform.branches.application.service;

import com.company.platform.branches.api.dto.BranchResponse;
import com.company.platform.branches.api.dto.CreateBranchRequest;
import com.company.platform.branches.domain.enumtype.BranchStatus;
import com.company.platform.branches.domain.model.Branch;
import com.company.platform.branches.infrastructure.repository.BranchRepository;
import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.common.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    public BranchResponse createBranch(CreateBranchRequest request) {
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
