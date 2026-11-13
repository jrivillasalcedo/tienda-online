package com.tienda.pedido;

import com.tienda.error.PedidoNoEncontradoException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService service) {
        this.pedidoService = service;
    }

    @GetMapping
    public List<PedidoDto> getAll() {
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public PedidoDto getById(@PathVariable Long id) {
        return pedidoService.buscarPorId(id).orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    @PostMapping("/{id}/cancelar")
    public PedidoDto cancelar(@PathVariable Long id) {
        return pedidoService.cancelar(id);
    }

    @PostMapping
    public ResponseEntity<PedidoDto> crear(@Valid @RequestBody CrearPedidoRequest request) {
        PedidoDto creado = pedidoService.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }
}



