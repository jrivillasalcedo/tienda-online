package com.tienda.error;

public class SkuDesconocidoException extends RuntimeException {
    public SkuDesconocidoException(String sku) {
        super("El SKU '" + sku + "' no existe en el catálogo");
    }
}

