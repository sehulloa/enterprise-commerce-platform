package com.company.platform.branches.application.service;

import com.company.platform.branches.api.dto.BranchResponse;
import com.company.platform.branches.api.dto.CreateBranchRequest;
import com.company.platform.branches.api.dto.UpdateBranchStatusRequest;

import java.util.List;

public interface BranchService {

    BranchResponse createBranch(CreateBranchRequest request);

    BranchResponse getBranchById(Long id);

    List<BranchResponse> getAllBranches();

    BranchResponse updateBranchStatus(Long id, UpdateBranchStatusRequest request);
}
