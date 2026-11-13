package com.tienda.pedido;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LineaRequest(
        @NotBlank(message = "el sku es obligatorio") String sku,
        @NotNull(message = "la cantidad es obligatoria")
        @Min(value = 1, message = "la cantidad debe ser al menos 1")
        @Max(value = 99, message = "la cantidad no puede ser mayor que 99") Integer cantidad) {}
