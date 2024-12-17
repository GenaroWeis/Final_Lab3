package ar.edu.utn.frbb.tup.model.enumModels;

public enum PrestamoEstados {
    APROBADO("A"),
    RECHAZADO("R");

    private final String codigo;

    PrestamoEstados(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static PrestamoEstados fromCodigo(String codigo) {
        for (PrestamoEstados estado : values()) {
            if (estado.getCodigo().equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Error: Código de estado del préstamo inválido: " + codigo);
    }
}

