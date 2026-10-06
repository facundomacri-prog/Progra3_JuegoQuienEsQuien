package estrategia;

import modelo.Personaje;
import modelo.Pregunta;

import java.util.List;

/** Strategy: la maquina delega sus decisiones en esta interfaz. */
public interface EstrategiaBusqueda {
    Pregunta elegirPregunta(List<Personaje> candidatos);
    boolean decidirSiAdivinar(List<Personaje> candidatos);
    Personaje seleccionarPersonajeSospechoso(List<Personaje> candidatos);
}
