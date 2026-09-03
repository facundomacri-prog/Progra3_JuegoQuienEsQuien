import Personaje;
import Pregunta;
import java.util.List;

public interface EstrategiaBusqueda {

    /**
     * Elige la pregunta a realizar basándose en la lista actual de candidatos.
     */
    Pregunta elegirPregunta(List<Personaje> candidatos);

    /**
     * Evalúa si, según la lógica de la máquina, conviene intentar adivinar en este turno.
     */
    boolean decidirSiAdivinar(List<Personaje> candidatos);

    /**
     * Selecciona cuál es el personaje al que se arriesgará a adivinar.
     */
    Personaje seleccionarPersonajeSospechoso(List<Personaje> candidatos);
}