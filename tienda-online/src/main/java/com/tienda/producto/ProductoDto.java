package com.tienda.producto;

import java.math.BigDecimal;

public record ProductoDto(String sku, String nombre, String categoria, BigDecimal precio) {}