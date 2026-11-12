package com.tienda.producto;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final List<ProductoDto> productos = new ArrayList<>();

    @PostConstruct

    void loaed() {
        productos.add(new ProductoDto("TEC-01", "Teclado mecánico", "PERIFERICOS", new BigDecimal("89.99")));
        productos.add(new ProductoDto("RAT-01", "Ratón inalámbrico", "PERIFERICOS", new BigDecimal("25.50")));
        productos.add(new ProductoDto("MON-01", "Monitor 27 pulgadas", "PANTALLAS", new BigDecimal("299.00")));
        productos.add(new ProductoDto("AUR-01", "Auriculares", "AUDIO", new BigDecimal("59.90")));
        productos.add(new ProductoDto("CAB-01", "Cable USB-C", "ACCESORIOS", new BigDecimal("9.99")));
    }

    @Override
    public List<ProductoDto> find(String categoria) {
        return productos.stream().filter(p -> categoria == null || categoria.isBlank() || p.categoria().equalsIgnoreCase(categoria)).toList();
    }

    @Override
    public Optional<ProductoDto> getBySku(String sku) {
        return productos.stream().filter(p -> p.sku().equalsIgnoreCase(sku)).findFirst();
    }
}
