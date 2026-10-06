package modelo;

/** Hay siete preguntas distintas. El humano puede repetirlas sin limite. */
public enum Pregunta {
    FEMENINO("¿Es de género femenino?"),
    MASCULINO("¿Es de género masculino?"),
    CALVICIE("¿Tiene calvicie?"),
    LENTES("¿Usa lentes?"),
    COLORADO("¿Tiene pelo colorado?"),
    NEGRO("¿Tiene pelo negro?"),
    AMARILLO("¿Tiene pelo amarillo?");

    private final String texto;

    Pregunta(String texto) { this.texto = texto; }

    public boolean coincide(Personaje personaje) {
        switch (this) {
            case FEMENINO: return personaje.getGenero() == Genero.FEMENINO;
            case MASCULINO: return personaje.getGenero() == Genero.MASCULINO;
            case CALVICIE: return personaje.tieneCalvicie();
            case LENTES: return personaje.tieneLentes();
            case COLORADO: return personaje.getColorPelo() == ColorPelo.COLORADO;
            case NEGRO: return personaje.getColorPelo() == ColorPelo.NEGRO;
            default: return personaje.getColorPelo() == ColorPelo.AMARILLO;
        }
    }

    public String toString() { return texto; }
}
