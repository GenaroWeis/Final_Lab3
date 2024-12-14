package ar.edu.utn.frbb.tup.model.enumModels;

public enum TipoPersona {

    PERSONA_FISICA("F"),
    PERSONA_JURIDICA("J");

    private final String descripcion;// final, no se puede cambiar una vez asignada

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    //convertir un texto en un valor del enum
    public static TipoPersona fromString(String text) {
        for (TipoPersona tipo : TipoPersona.values()) {// Recorre todas las constantes del enum
            if (tipo.descripcion.equalsIgnoreCase(text)) {// Compara la descripción de cada tipo con el texto proporcionado
                return tipo;// Retorna el tipo de moneda correspondiente si coincide
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar un TipoPersona con la descripción: " + text);
    }
}
