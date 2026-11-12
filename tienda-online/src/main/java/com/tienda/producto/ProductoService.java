package com.tienda.producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<ProductoDto> find(String categoria);
    Optional<ProductoDto> getBySku(String sku);
}
