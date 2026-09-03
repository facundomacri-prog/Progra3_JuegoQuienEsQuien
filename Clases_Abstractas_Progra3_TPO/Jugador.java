import Personaje;
import Pregunta;
import java.util.List;

public abstract class Jugador {

    protected String nombre;
    protected Personaje personajeSecreto;
    protected List<Personaje> candidatos;

    public Jugador(String nombre, Personaje personajeSecreto, List<Personaje> universoPersonajes) {
        this.nombre = nombre;
        this.personajeSecreto = personajeSecreto;
        this.candidatos = universoPersonajes;
    }

    // --- Decisiones de Turno ---

    //Se decide si se realizara una pregunta o se intentara adivinar
    public abstract boolean prefiereAdivinar();
    //Se elige cual pregunta se realizara
    public abstract Pregunta formularPregunta();
    //Se selecciona al sospechoso
    public abstract Personaje arriesgarPersonaje();


    // --- Metodos automaticos ---

    // Evalua si la pregunta del rival corresponde al personaje seleccionado por el jugador
    public abstract boolean evaluarPreguntaRival(Pregunta pregunta);
    // Evalua si el personaje del rival es el seleccionado por el jugador
    public abstract boolean esMiPersonajeSecreto(Personaje personajeArriesgado);
    // En base a la respuesta del rival, se descartan de la lista candidatos
    public abstract void descartarCandidatos(Pregunta pregunta, boolean respuestaRival);


    // --- Getters ---

    public String getNombre() {
        return nombre;
    }

    public Personaje getPersonajeSecreto() {
        return personajeSecreto;
    }

    public List<Personaje> getCandidatos() {
        return candidatos;
    }
}