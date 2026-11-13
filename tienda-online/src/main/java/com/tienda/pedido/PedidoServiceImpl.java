package com.tienda.pedido;

import com.tienda.error.EstadoInvalidoException;
import com.tienda.error.PedidoNoEncontradoException;
import com.tienda.error.SkuDesconocidoException;
import com.tienda.error.SolicitudInvalidaException;
import com.tienda.producto.ProductoDto;
import com.tienda.producto.ProductoService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final Map<Long, PedidoDto> pedidos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();
    private final ProductoService productoService;

    public PedidoServiceImpl(ProductoService service) {
        this.productoService = service;
    }

    @PostConstruct
    void load() {
        semilla("ana@mail.com", 5, EstadoPedido.ENTREGADO,
                LineaPedidoDto.of("TEC-01", "Teclado mecánico", 1, new BigDecimal("89.99")),
                LineaPedidoDto.of("RAT-01", "Ratón inalámbrico", 1, new BigDecimal("25.50")));
        semilla("luis@mail.com", 4, EstadoPedido.ENVIADO,
                LineaPedidoDto.of("MON-01", "Monitor 27 pulgadas", 2, new BigDecimal("299.00")));
        semilla("marta@mail.com", 3, EstadoPedido.PENDIENTE,
                LineaPedidoDto.of("AUR-01", "Auriculares", 1, new BigDecimal("59.90")),
                LineaPedidoDto.of("CAB-01", "Cable USB-C", 3, new BigDecimal("9.99")));
        semilla("ana@mail.com", 2, EstadoPedido.CANCELADO,
                LineaPedidoDto.of("TEC-01", "Teclado mecánico", 1, new BigDecimal("89.99")));
        semilla("pablo@mail.com", 1, EstadoPedido.PENDIENTE,
                LineaPedidoDto.of("RAT-01", "Ratón inalámbrico", 2, new BigDecimal("25.50")),
                LineaPedidoDto.of("CAB-01", "Cable USB-C", 1, new BigDecimal("9.99")));
    }

    private void semilla(String cliente, int diasAtras, EstadoPedido estado, LineaPedidoDto... lineas) {
        List<LineaPedidoDto> lista = List.of(lineas);
        PedidoDto pedido = new PedidoDto(secuencia.incrementAndGet(), cliente,
                Instant.now().minus(diasAtras, ChronoUnit.DAYS), estado, lista, total(lista));
        pedidos.put(pedido.id(), pedido);
    }

    private static BigDecimal total(List<LineaPedidoDto> lineas) {
        return lineas.stream().map(LineaPedidoDto::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public PedidoDto crear(CrearPedidoRequest request) {
        Set<String> vistos = new HashSet<>();
        for (int i = 0; i < request.lineas().size(); i++) {
            String sku = request.lineas().get(i).sku().trim().toUpperCase();
            if (!vistos.add(sku)) {
                throw new SolicitudInvalidaException("lineas[" + i + "].sku",
                        "el sku '" + sku + "' está repetido; agrupa las unidades en una sola línea");
            }
        }

        List<LineaPedidoDto> lineas = request.lineas().stream()
                .map(l -> {
                    ProductoDto producto = productoService.getBySku(l.sku())
                            .orElseThrow(() -> new SkuDesconocidoException(l.sku()));
                    return LineaPedidoDto.of(producto.sku(), producto.nombre(), l.cantidad(), producto.precio());
                })
                .toList();

        PedidoDto pedido = new PedidoDto(
                secuencia.incrementAndGet(),
                request.cliente(),
                Instant.now(),
                EstadoPedido.PENDIENTE,
                lineas,
                total(lineas));

        pedidos.put(pedido.id(), pedido);
        return pedido;
    }

    @Override
    public List<PedidoDto> listar() {
        return pedidos.values().stream().sorted(Comparator.comparing(PedidoDto::id)).toList();
    }

    @Override
    public Optional<PedidoDto> buscarPorId(Long id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    @Override
    public PedidoDto cancelar(Long id) {
        return pedidos.compute(id, (clave, pedido) -> {
            if (pedido == null) {
                throw new PedidoNoEncontradoException(id);
            }
            if (pedido.estado() != EstadoPedido.PENDIENTE && pedido.estado() != EstadoPedido.PAGADO) {
                throw new EstadoInvalidoException(id, pedido.estado());
            }
            return new PedidoDto(pedido.id(), pedido.cliente(), pedido.fecha(), EstadoPedido.CANCELADO,
                    pedido.lineas(), pedido.total());
        });
    }
}
