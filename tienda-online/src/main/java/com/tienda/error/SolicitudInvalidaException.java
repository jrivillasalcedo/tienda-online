package com.tienda.error;

public class SolicitudInvalidaException extends RuntimeException {
    private final String campo;

    public SolicitudInvalidaException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}

