package modelo;

public enum Genero {
    FEMENINO("Femenino"), MASCULINO("Masculino");

    private final String texto;

    Genero(String texto) { this.texto = texto; }

    public String toString() { return texto; }
}
