package com.tienda.pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    PedidoDto crear(CrearPedidoRequest request);
    List<PedidoDto> listar();
    Optional<PedidoDto> buscarPorId(Long id);
    PedidoDto cancelar(Long id);
}
