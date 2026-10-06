package modelo;

public enum ColorPelo {
    COLORADO("Colorado"), NEGRO("Negro"), AMARILLO("Amarillo");

    private final String texto;

    ColorPelo(String texto) { this.texto = texto; }

    public String toString() { return texto; }
}
