package com.autocare.parts.controller;

import com.autocare.parts.dto.SparePartRequest;
import com.autocare.parts.dto.SparePartResponse;
import com.autocare.parts.dto.StockUpdateRequest;
import com.autocare.parts.service.SparePartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parts")
public class SparePartController {

    private final SparePartService sparePartService;

    public SparePartController(
            SparePartService sparePartService
    ) {
        this.sparePartService =
                sparePartService;
    }

    @GetMapping
    public List<SparePartResponse> getAllParts() {

        return sparePartService
                .getAllParts();
    }

    @PostMapping
    public ResponseEntity<SparePartResponse> addPart(
            @Valid
            @RequestBody
            SparePartRequest request
    ) {

        SparePartResponse response =
                sparePartService.addPart(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public SparePartResponse updatePart(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            SparePartRequest request
    ) {

        return sparePartService
                .updatePart(
                        id,
                        request
                );
    }

    @PutMapping("/{id}/stock")
    public SparePartResponse updateStock(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            StockUpdateRequest request
    ) {

        return sparePartService
                .updateStock(
                        id,
                        request
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePart(
            @PathVariable UUID id
    ) {

        sparePartService.deletePart(
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}