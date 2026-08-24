package com.autocare.parts.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class SparePartResponse {

    private UUID id;
    private String name;
    private String partNumber;
    private Integer quantity;
    private BigDecimal price;

    public SparePartResponse() {
    }

    public SparePartResponse(
            UUID id,
            String name,
            String partNumber,
            Integer quantity,
            BigDecimal price
    ) {
        this.id = id;
        this.name = name;
        this.partNumber = partNumber;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}