package com.tienda.error;

import com.tienda.pedido.EstadoPedido;

public class EstadoInvalidoException extends RuntimeException {
    public EstadoInvalidoException(Long id, EstadoPedido estado) {
        super("No se puede cancelar el pedido '" + id + "' porque está en estado " + estado);
    }
}

