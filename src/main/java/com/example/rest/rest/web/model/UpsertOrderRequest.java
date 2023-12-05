package com.example.rest.rest.web.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertOrderRequest {

    private Long clientId;

    @NotBlank(message = "Название продукта не должно быть пустым")
    private String product;
    private BigDecimal cost;
}
