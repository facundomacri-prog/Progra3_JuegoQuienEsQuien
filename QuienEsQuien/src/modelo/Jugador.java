package modelo;

/** Un rival solo recibe respuestas booleanas. No existe un getter del secreto. */
public abstract class Jugador {
    private final String nombre;
    private final Personaje personajeSecreto;

    protected Jugador(String nombre, Personaje personajeSecreto) {
        this.nombre = nombre;
        this.personajeSecreto = personajeSecreto;
    }

    public String getNombre() { return nombre; }

    public boolean evaluarPreguntaRival(Pregunta pregunta) {
        return pregunta.coincide(personajeSecreto);
    }

    public boolean esMiPersonajeSecreto(Personaje personajeArriesgado) {
        return personajeSecreto.getId() == personajeArriesgado.getId();
    }
}
