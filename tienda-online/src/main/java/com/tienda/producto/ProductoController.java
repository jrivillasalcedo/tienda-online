package com.tienda.producto;

import com.tienda.pedido.PedidoDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService service) {
        this.productoService = service;
    }

    @GetMapping
    public List<ProductoDto> getAllProdctos(@RequestParam(required = false) String categoria) {
        return productoService.find(categoria);
    }

    @GetMapping("/{sku}")
    public ProductoDto getBySku(@PathVariable String sku) {
        return productoService.getBySku(sku).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}



