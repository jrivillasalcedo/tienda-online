package com.tienda.pedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CrearPedidoRequest(
        @NotBlank(message = "el cliente es obligatorio")
        @Email(message = "el cliente debe ser un email válido") String cliente,
        @NotEmpty(message = "debe haber al menos una línea") @Valid List<LineaRequest> lineas) {}
