package com.tienda.pedido;

import java.math.BigDecimal;

public record LineaPedidoDto(String sku, String nombre, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {

    public static LineaPedidoDto of(String sku, String nombre, int cantidad, BigDecimal precioUnitario) {
        return new LineaPedidoDto(sku, nombre, cantidad, precioUnitario,
                precioUnitario.multiply(BigDecimal.valueOf(cantidad)));
    }
}
