package com.autocare.parts.controller;

import com.autocare.parts.dto.SparePartRequest;
import com.autocare.parts.dto.SparePartResponse;
import com.autocare.parts.exception.GlobalExceptionHandler;
import com.autocare.parts.exception.SparePartNotFoundException;
import com.autocare.parts.service.SparePartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SparePartControllerApiTest {

    private SparePartService sparePartService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        sparePartService =
                mock(SparePartService.class);

        SparePartController controller =
                new SparePartController(
                        sparePartService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(controller)
                        .setControllerAdvice(
                                new GlobalExceptionHandler()
                        )
                        .build();
    }

    @Test
    void getAllPartsShouldReturnOk() throws Exception {

        SparePartResponse response =
                new SparePartResponse(
                        UUID.randomUUID(),
                        "Oil Filter",
                        "OF-100",
                        10,
                        new BigDecimal("24.90")
                );

        when(
                sparePartService.getAllParts()
        ).thenReturn(
                List.of(response)
        );

        mockMvc.perform(
                        get("/api/parts")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath(
                                "$[0].name"
                        ).value(
                                "Oil Filter"
                        )
                )
                .andExpect(
                        jsonPath(
                                "$[0].partNumber"
                        ).value(
                                "OF-100"
                        )
                );
    }

    @Test
    void addPartShouldReturnCreated() throws Exception {

        SparePartResponse response =
                new SparePartResponse(
                        UUID.randomUUID(),
                        "Brake Pads",
                        "BP-200",
                        5,
                        new BigDecimal("89.90")
                );

        when(
                sparePartService.addPart(
                        any(SparePartRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        post("/api/parts")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        """
                                        {
                                          "name": "Brake Pads",
                                          "partNumber": "BP-200",
                                          "quantity": 5,
                                          "price": 89.90
                                        }
                                        """
                                )
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath(
                                "$.name"
                        ).value(
                                "Brake Pads"
                        )
                )
                .andExpect(
                        jsonPath(
                                "$.quantity"
                        ).value(5)
                );
    }

    @Test
    void updateMissingPartShouldReturnNotFound()
            throws Exception {

        UUID id =
                UUID.randomUUID();

        when(
                sparePartService.updatePart(
                        eq(id),
                        any(SparePartRequest.class)
                )
        ).thenThrow(
                new SparePartNotFoundException(
                        "Spare part not found"
                )
        );

        mockMvc.perform(
                        put(
                                "/api/parts/{id}",
                                id
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        """
                                        {
                                          "name": "Brake Pads",
                                          "partNumber": "BP-200",
                                          "quantity": 5,
                                          "price": 89.90
                                        }
                                        """
                                )
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath(
                                "$.message"
                        ).value(
                                "Spare part not found"
                        )
                );
    }

    @Test
    void invalidRequestShouldReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                        post("/api/parts")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        """
                                        {
                                          "name": "",
                                          "partNumber": "",
                                          "quantity": -1,
                                          "price": 0
                                        }
                                        """
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );
    }
}