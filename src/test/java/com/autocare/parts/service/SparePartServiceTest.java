package com.autocare.parts.service;

import com.autocare.parts.dto.SparePartRequest;
import com.autocare.parts.dto.SparePartResponse;
import com.autocare.parts.dto.StockUpdateRequest;
import com.autocare.parts.entity.SparePart;
import com.autocare.parts.exception.SparePartNotFoundException;
import com.autocare.parts.repository.SparePartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SparePartServiceTest {

    @Mock
    private SparePartRepository sparePartRepository;

    private SparePartService sparePartService;

    @BeforeEach
    void setUp() {
        sparePartService =
                new SparePartService(
                        sparePartRepository
                );
    }

    @Test
    void getAllPartsShouldReturnMappedParts() {

        UUID id = UUID.randomUUID();

        SparePart sparePart =
                createSparePart(
                        id,
                        "Oil Filter",
                        "OF-100",
                        10,
                        new BigDecimal("24.90")
                );

        when(
                sparePartRepository
                        .findAllByOrderByNameAsc()
        ).thenReturn(
                List.of(sparePart)
        );

        List<SparePartResponse> result =
                sparePartService.getAllParts();

        assertEquals(1, result.size());

        assertEquals(
                "Oil Filter",
                result.get(0).getName()
        );

        assertEquals(
                "OF-100",
                result.get(0).getPartNumber()
        );

        assertEquals(
                10,
                result.get(0).getQuantity()
        );
    }

    @Test
    void addPartShouldCreatePart() {

        SparePartRequest request =
                createRequest(
                        "Brake Pads",
                        " bp-200 ",
                        5,
                        new BigDecimal("89.90")
                );

        when(
                sparePartRepository
                        .existsByPartNumberIgnoreCase(
                                "BP-200"
                        )
        ).thenReturn(false);

        when(
                sparePartRepository.save(
                        any(SparePart.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        SparePartResponse response =
                sparePartService.addPart(
                        request
                );

        assertEquals(
                "Brake Pads",
                response.getName()
        );

        assertEquals(
                "BP-200",
                response.getPartNumber()
        );

        assertEquals(
                5,
                response.getQuantity()
        );

        verify(
                sparePartRepository
        ).save(
                any(SparePart.class)
        );
    }

    @Test
    void addPartShouldRejectDuplicatePartNumber() {

        SparePartRequest request =
                createRequest(
                        "Brake Pads",
                        "BP-200",
                        5,
                        new BigDecimal("89.90")
                );

        when(
                sparePartRepository
                        .existsByPartNumberIgnoreCase(
                                "BP-200"
                        )
        ).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        sparePartService
                                .addPart(request)
        );

        verify(
                sparePartRepository,
                never()
        ).save(
                any(SparePart.class)
        );
    }

    @Test
    void updateStockShouldChangeQuantity() {

        UUID id = UUID.randomUUID();

        SparePart sparePart =
                createSparePart(
                        id,
                        "Air Filter",
                        "AF-300",
                        2,
                        new BigDecimal("19.90")
                );

        StockUpdateRequest request =
                new StockUpdateRequest();

        request.setQuantity(12);

        when(
                sparePartRepository.findById(id)
        ).thenReturn(
                Optional.of(sparePart)
        );

        SparePartResponse response =
                sparePartService.updateStock(
                        id,
                        request
                );

        assertEquals(
                12,
                response.getQuantity()
        );

        verify(
                sparePartRepository
        ).save(sparePart);
    }

    @Test
    void deletePartShouldDeleteExistingPart() {

        UUID id = UUID.randomUUID();

        SparePart sparePart =
                createSparePart(
                        id,
                        "Spark Plug",
                        "SP-500",
                        8,
                        new BigDecimal("14.50")
                );

        when(
                sparePartRepository.findById(id)
        ).thenReturn(
                Optional.of(sparePart)
        );

        sparePartService.deletePart(id);

        verify(
                sparePartRepository
        ).delete(sparePart);
    }

    @Test
    void missingPartShouldThrowException() {

        UUID id = UUID.randomUUID();

        when(
                sparePartRepository.findById(id)
        ).thenReturn(
                Optional.empty()
        );

        StockUpdateRequest request =
                new StockUpdateRequest();

        request.setQuantity(5);

        assertThrows(
                SparePartNotFoundException.class,
                () ->
                        sparePartService
                                .updateStock(
                                        id,
                                        request
                                )
        );
    }

    private SparePartRequest createRequest(
            String name,
            String partNumber,
            Integer quantity,
            BigDecimal price
    ) {

        SparePartRequest request =
                new SparePartRequest();

        request.setName(name);
        request.setPartNumber(partNumber);
        request.setQuantity(quantity);
        request.setPrice(price);

        return request;
    }

    private SparePart createSparePart(
            UUID id,
            String name,
            String partNumber,
            Integer quantity,
            BigDecimal price
    ) {

        SparePart sparePart =
                new SparePart();

        sparePart.setId(id);
        sparePart.setName(name);
        sparePart.setPartNumber(partNumber);
        sparePart.setQuantity(quantity);
        sparePart.setPrice(price);

        return sparePart;
    }
}