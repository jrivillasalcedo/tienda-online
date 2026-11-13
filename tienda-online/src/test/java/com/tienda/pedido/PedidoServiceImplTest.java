package com.tienda.pedido;

import com.tienda.error.EstadoInvalidoException;
import com.tienda.error.PedidoNoEncontradoException;
import com.tienda.error.SkuDesconocidoException;
import com.tienda.error.SolicitudInvalidaException;
import com.tienda.producto.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceImplTest {

    private PedidoServiceImpl service;

    @BeforeEach
    void setUp() {
        ProductoServiceImpl productos = new ProductoServiceImpl();
        productos.load();
        service = new PedidoServiceImpl(productos);
        service.load();
    }

    private CrearPedidoRequest request(String sku, int cantidad) {
        return new CrearPedidoRequest("test@mail.com", List.of(new LineaRequest(sku, cantidad)));
    }

    @Test
    void creaPedidoConDatosDelCatalogo() {
        PedidoDto p = service.crear(request("TEC-01", 2));
        assertEquals(6L, p.id());
        assertEquals(EstadoPedido.PENDIENTE, p.estado());
        assertEquals("Teclado mecánico", p.lineas().get(0).nombre());
        assertEquals(new BigDecimal("179.98"), p.total());
        assertTrue(service.buscarPorId(p.id()).isPresent());
    }

    @Test
    void skuDesconocido() {
        assertThrows(SkuDesconocidoException.class, () -> service.crear(request("XXX", 1)));
    }

    @Test
    void skuRepetido() {
        CrearPedidoRequest r = new CrearPedidoRequest("a@b.com",
                List.of(new LineaRequest("TEC-01", 1), new LineaRequest("tec-01", 1)));
        assertThrows(SolicitudInvalidaException.class, () -> service.crear(r));
    }

    @Test
    void listaOrdenadaPorId() {
        assertEquals(List.of(1L, 2L, 3L, 4L, 5L), service.listar().stream().map(PedidoDto::id).toList());
    }

    @Test
    void cancelaPendiente() {
        assertEquals(EstadoPedido.CANCELADO, service.cancelar(3L).estado());
    }

    @Test
    void noCancelaDosVecesNiEnviado() {
        service.cancelar(3L);
        assertThrows(EstadoInvalidoException.class, () -> service.cancelar(3L));
        assertThrows(EstadoInvalidoException.class, () -> service.cancelar(2L));
    }

    @Test
    void cancelarInexistente() {
        assertThrows(PedidoNoEncontradoException.class, () -> service.cancelar(99L));
    }
}

