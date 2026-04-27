package com.company.platform.inventory.application.service;

import com.company.platform.common.api.exception.BusinessException;
import com.company.platform.common.api.exception.NotFoundException;
import com.company.platform.common.api.exception.ResourceAlreadyExistsException;
import com.company.platform.inventory.api.dto.AdjustStockRequest;
import com.company.platform.inventory.api.dto.CreateInventoryItemRequest;
import com.company.platform.inventory.domain.enumtype.StockMovementType;
import com.company.platform.inventory.domain.model.InventoryItem;
import com.company.platform.inventory.domain.model.StockMovement;
import com.company.platform.inventory.infrastructure.repository.InventoryItemRepository;
import com.company.platform.inventory.infrastructure.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final StockMovementRepository stockMovementRepository;

    private static final String ITEM_NFOUND_MSG = "Inventory item not found";

    @Override
    @Transactional
    public InventoryItem createInventoryItem(CreateInventoryItemRequest request) {

        log.info("Creating inventory item for branchId={} productId={} totalQuantity={}",
                request.getBranchId(),
                request.getProductId(),
                request.getTotalQuantity());

        inventoryItemRepository.findByBranchIdAndProductId(request.getBranchId(), request.getProductId())
                .ifPresent(item -> {
                    throw new ResourceAlreadyExistsException("Inventory item already exists for branch and product");
                });

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setBranchId(request.getBranchId());
        inventoryItem.setProductId(request.getProductId());
        inventoryItem.setTotalQuantity(request.getTotalQuantity());
        inventoryItem.setReservedQuantity(0);
        inventoryItem.setActive(true);

        InventoryItem savedItem = inventoryItemRepository.save(inventoryItem);

        if (request.getTotalQuantity() > 0) {
            saveMovement(savedItem.getId(), StockMovementType.INBOUND, request.getTotalQuantity(), "INITIAL_STOCK", "Initial stock creation");
        }

        log.info("Inventory item created successfully with inventoryItemId={} branchId={} productId={}",
                savedItem.getId(),
                savedItem.getBranchId(),
                savedItem.getProductId());

        return savedItem;
    }

    @Override
    @Transactional
    public InventoryItem inboundStock(AdjustStockRequest request) {

        log.info("Applying inbound stock for inventoryItemId={} quantity={}",
                request.getInventoryItemId(),
                request.getQuantity());

        InventoryItem inventoryItem = findInventoryItemOrThrow(request.getInventoryItemId());

        inventoryItem.setTotalQuantity(inventoryItem.getTotalQuantity() + request.getQuantity());

        InventoryItem savedItem = inventoryItemRepository.save(inventoryItem);

        saveMovement(savedItem.getId(), StockMovementType.INBOUND, request.getQuantity(), request.getReference(), request.getNotes());

        log.info("Inbound stock applied successfully for inventoryItemId={} totalQuantity={}",
                savedItem.getId(),
                savedItem.getTotalQuantity());

        return savedItem;
    }

    @Override
    @Transactional
    public InventoryItem adjustmentIn(AdjustStockRequest request) {

        log.info("Applying positive stock adjustment for inventoryItemId={} quantity={}",
                request.getInventoryItemId(),
                request.getQuantity());

        InventoryItem inventoryItem = findInventoryItemOrThrow(request.getInventoryItemId());

        inventoryItem.setTotalQuantity(inventoryItem.getTotalQuantity() + request.getQuantity());

        InventoryItem savedItem = inventoryItemRepository.save(inventoryItem);

        saveMovement(savedItem.getId(), StockMovementType.ADJUSTMENT_IN, request.getQuantity(), request.getReference(), request.getNotes());

        log.info("Positive stock adjustment applied for inventoryItemId={} totalQuantity={}",
                savedItem.getId(),
                savedItem.getTotalQuantity());

        return savedItem;
    }


    @Override
    @Transactional
    public InventoryItem adjustmentOut(AdjustStockRequest request) {

        log.info("Applying negative stock adjustment for inventoryItemId={} quantity={}",
                request.getInventoryItemId(),
                request.getQuantity());

        InventoryItem inventoryItem = findInventoryItemOrThrow(request.getInventoryItemId());

        if (inventoryItem.getTotalQuantity() - request.getQuantity() < inventoryItem.getReservedQuantity()) {
            throw new BusinessException("Cannot reduce stock below reserved quantity");
        }

        inventoryItem.setTotalQuantity(inventoryItem.getTotalQuantity() - request.getQuantity());

        InventoryItem savedItem = inventoryItemRepository.save(inventoryItem);

        saveMovement(savedItem.getId(), StockMovementType.ADJUSTMENT_OUT, request.getQuantity(), request.getReference(), request.getNotes());

        log.info("Negative stock adjustment applied for inventoryItemId={} totalQuantity={}",
                savedItem.getId(),
                savedItem.getTotalQuantity());

        return savedItem;
    }

    @Override
    @Transactional(readOnly = true)
    public int getAvailableStock(Long branchId, Long productId) {
        InventoryItem inventoryItem = inventoryItemRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new RuntimeException(ITEM_NFOUND_MSG));

        return inventoryItem.getTotalQuantity() - inventoryItem.getReservedQuantity();
    }

    @Override
    @Transactional
    public void consumeStock(Long branchId, Long productId, Integer quantity) {

        log.info("Consuming stock for branchId={} productId={} quantity={}",
                branchId,
                productId,
                quantity);

        InventoryItem item = inventoryItemRepository
                .findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new NotFoundException(ITEM_NFOUND_MSG));

        int available = item.getTotalQuantity() - item.getReservedQuantity();

        if (available < quantity) {
            throw new BusinessException("Not enough stock available to confirm order");
        }

        item.setTotalQuantity(item.getTotalQuantity() - quantity);

        inventoryItemRepository.save(item);

        saveMovement(
                item.getId(),
                StockMovementType.OUTBOUND,
                quantity,
                "ORDER_CONFIRMATION",
                "Stock consumed by order confirmation"
        );

        log.info("Stock consumed successfully for inventoryItemId={} remainingTotalQuantity={}",
                item.getId(),
                item.getTotalQuantity());
    }

    private InventoryItem findInventoryItemOrThrow(Long inventoryItemId) {
        return inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new RuntimeException(ITEM_NFOUND_MSG));
    }

    private void saveMovement(Long inventoryItemId,
                              StockMovementType movementType,
                              Integer quantity,
                              String reference,
                              String notes) {
        StockMovement movement = new StockMovement();
        movement.setInventoryItemId(inventoryItemId);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setReference(reference);
        movement.setNotes(notes);

        stockMovementRepository.save(movement);
    }
}
