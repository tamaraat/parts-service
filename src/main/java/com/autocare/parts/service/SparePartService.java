package com.autocare.parts.service;

import com.autocare.parts.dto.SparePartRequest;
import com.autocare.parts.dto.SparePartResponse;
import com.autocare.parts.dto.StockUpdateRequest;
import com.autocare.parts.entity.SparePart;
import com.autocare.parts.exception.SparePartNotFoundException;
import com.autocare.parts.repository.SparePartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SparePartService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    SparePartService.class
            );

    private final SparePartRepository sparePartRepository;

    public SparePartService(
            SparePartRepository sparePartRepository
    ) {
        this.sparePartRepository =
                sparePartRepository;
    }

    @Cacheable("spareParts")
    public List<SparePartResponse> getAllParts() {

        return sparePartRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(
            value = "spareParts",
            allEntries = true
    )
    public SparePartResponse addPart(
            SparePartRequest request
    ) {

        String partNumber =
                normalizePartNumber(
                        request.getPartNumber()
                );

        if (sparePartRepository
                .existsByPartNumberIgnoreCase(
                        partNumber
                )) {

            throw new IllegalArgumentException(
                    "A spare part with this part number already exists"
            );
        }

        validateQuantity(
                request.getQuantity()
        );

        SparePart sparePart =
                new SparePart();

        sparePart.setName(
                request.getName().trim()
        );

        sparePart.setPartNumber(
                partNumber
        );

        sparePart.setQuantity(
                request.getQuantity()
        );

        sparePart.setPrice(
                request.getPrice()
        );

        sparePartRepository.save(
                sparePart
        );

        log.info(
                "Created spare part with id {}",
                sparePart.getId()
        );

        return mapToResponse(
                sparePart
        );
    }

    @Transactional
    @CacheEvict(
            value = "spareParts",
            allEntries = true
    )
    public SparePartResponse updatePart(
            UUID id,
            SparePartRequest request
    ) {

        SparePart sparePart =
                findPart(id);

        String partNumber =
                normalizePartNumber(
                        request.getPartNumber()
                );

        if (sparePartRepository
                .existsByPartNumberIgnoreCaseAndIdNot(
                        partNumber,
                        id
                )) {

            throw new IllegalArgumentException(
                    "A spare part with this part number already exists"
            );
        }

        validateQuantity(
                request.getQuantity()
        );

        sparePart.setName(
                request.getName().trim()
        );

        sparePart.setPartNumber(
                partNumber
        );

        sparePart.setQuantity(
                request.getQuantity()
        );

        sparePart.setPrice(
                request.getPrice()
        );

        sparePartRepository.save(
                sparePart
        );

        log.info(
                "Updated spare part with id {}",
                id
        );

        return mapToResponse(
                sparePart
        );
    }

    @Transactional
    @CacheEvict(
            value = "spareParts",
            allEntries = true
    )
    public SparePartResponse updateStock(
            UUID id,
            StockUpdateRequest request
    ) {

        validateQuantity(
                request.getQuantity()
        );

        SparePart sparePart =
                findPart(id);

        sparePart.setQuantity(
                request.getQuantity()
        );

        sparePartRepository.save(
                sparePart
        );

        log.info(
                "Changed stock quantity of spare part {} to {}",
                id,
                request.getQuantity()
        );

        return mapToResponse(
                sparePart
        );
    }

    @Transactional
    @CacheEvict(
            value = "spareParts",
            allEntries = true
    )
    public void deletePart(
            UUID id
    ) {

        SparePart sparePart =
                findPart(id);

        sparePartRepository.delete(
                sparePart
        );

        log.info(
                "Deleted spare part with id {}",
                id
        );
    }

    private SparePart findPart(
            UUID id
    ) {

        return sparePartRepository
                .findById(id)
                .orElseThrow(() ->
                        new SparePartNotFoundException(
                                "Spare part not found"
                        )
                );
    }

    private String normalizePartNumber(
            String partNumber
    ) {

        if (partNumber == null ||
                partNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Part number is required"
            );
        }

        return partNumber
                .trim()
                .toUpperCase();
    }

    private void validateQuantity(
            Integer quantity
    ) {

        if (quantity == null ||
                quantity < 0) {

            throw new IllegalArgumentException(
                    "Quantity cannot be negative"
            );
        }
    }

    private SparePartResponse mapToResponse(
            SparePart sparePart
    ) {

        return new SparePartResponse(
                sparePart.getId(),
                sparePart.getName(),
                sparePart.getPartNumber(),
                sparePart.getQuantity(),
                sparePart.getPrice()
        );
    }
}