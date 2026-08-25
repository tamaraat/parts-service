package com.autocare.parts.repository;

import com.autocare.parts.entity.SparePart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SparePartRepositoryIntegrationTest {

    @Autowired
    private SparePartRepository sparePartRepository;

    @Test
    void shouldSaveAndFindSparePart() {

        String uniquePartNumber =
                "TEST-" + UUID.randomUUID();

        SparePart sparePart =
                new SparePart();

        sparePart.setName(
                "Integration Test Filter"
        );

        sparePart.setPartNumber(
                uniquePartNumber
        );

        sparePart.setQuantity(7);

        sparePart.setPrice(
                new BigDecimal("29.90")
        );

        SparePart savedPart =
                sparePartRepository.save(
                        sparePart
                );

        Optional<SparePart> foundPart =
                sparePartRepository.findById(
                        savedPart.getId()
                );

        assertTrue(
                foundPart.isPresent()
        );

        assertEquals(
                "Integration Test Filter",
                foundPart.get().getName()
        );

        assertEquals(
                uniquePartNumber,
                foundPart.get().getPartNumber()
        );
    }
}