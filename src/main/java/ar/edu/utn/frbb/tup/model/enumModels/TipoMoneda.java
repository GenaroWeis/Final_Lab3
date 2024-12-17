package ar.edu.utn.frbb.tup.model.enumModels;

// logica de tipopersona
public enum TipoMoneda {
    PESOS("P"),
    DOLARES("D");

    private final String descripcion;

    TipoMoneda(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoMoneda fromString(String text) {
        for (TipoMoneda tipo : TipoMoneda.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Error: No se encontró un TipoMoneda con la descripción: " + text);
    }
}
