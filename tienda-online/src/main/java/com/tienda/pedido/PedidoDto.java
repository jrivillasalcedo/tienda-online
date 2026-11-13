package com.tienda.pedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PedidoDto(Long id, String cliente, Instant fecha, EstadoPedido estado, List<LineaPedidoDto> lineas, BigDecimal total) {}
