package com.autocare.parts.scheduler;

import com.autocare.parts.entity.SparePart;
import com.autocare.parts.repository.SparePartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventorySchedulerTest {

    @Mock
    private SparePartRepository sparePartRepository;

    private InventoryScheduler inventoryScheduler;

    @BeforeEach
    void setUp() {
        inventoryScheduler =
                new InventoryScheduler(
                        sparePartRepository
                );
    }

    @Test
    void restockLowStockPartsShouldRestockPartsBelowThreshold() {

        SparePart lowStockPart =
                createPart(
                        "Oil Filter",
                        "OF-100",
                        2
                );

        SparePart normalStockPart =
                createPart(
                        "Brake Pads",
                        "BP-200",
                        8
                );

        when(
                sparePartRepository.findAll()
        ).thenReturn(
                List.of(
                        lowStockPart,
                        normalStockPart
                )
        );

        inventoryScheduler
                .restockLowStockParts();

        assertEquals(
                10,
                lowStockPart.getQuantity()
        );

        assertEquals(
                8,
                normalStockPart.getQuantity()
        );

        ArgumentCaptor<List<SparePart>> captor =
                ArgumentCaptor.forClass(
                        List.class
                );

        verify(
                sparePartRepository
        ).saveAll(
                captor.capture()
        );

        assertEquals(
                1,
                captor.getValue().size()
        );

        assertSame(
                lowStockPart,
                captor.getValue().get(0)
        );
    }

    @Test
    void restockLowStockPartsShouldDoNothingWhenStockIsEnough() {

        SparePart part =
                createPart(
                        "Air Filter",
                        "AF-300",
                        5
                );

        when(
                sparePartRepository.findAll()
        ).thenReturn(
                List.of(part)
        );

        inventoryScheduler
                .restockLowStockParts();

        assertEquals(
                5,
                part.getQuantity()
        );

        verify(
                sparePartRepository,
                never()
        ).saveAll(
                any()
        );
    }

    @Test
    void restockShouldIgnoreNullQuantity() {

        SparePart part =
                createPart(
                        "Test Part",
                        "TP-100",
                        null
                );

        when(
                sparePartRepository.findAll()
        ).thenReturn(
                List.of(part)
        );

        inventoryScheduler
                .restockLowStockParts();

        verify(
                sparePartRepository,
                never()
        ).saveAll(
                any()
        );
    }

    @Test
    void refreshCacheShouldRunWithoutError() {

        assertDoesNotThrow(
                () ->
                        inventoryScheduler
                                .refreshSparePartsCache()
        );
    }

    private SparePart createPart(
            String name,
            String partNumber,
            Integer quantity
    ) {

        SparePart sparePart =
                new SparePart();

        sparePart.setName(name);
        sparePart.setPartNumber(
                partNumber
        );
        sparePart.setQuantity(
                quantity
        );
        sparePart.setPrice(
                new BigDecimal("20.00")
        );

        return sparePart;
    }
}