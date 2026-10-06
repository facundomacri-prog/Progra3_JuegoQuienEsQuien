package estrategia;

import modelo.Personaje;
import modelo.Pregunta;

import java.util.List;

/** Pregunta en orden fijo. Con un único candidato, arriesga con certeza. */
public class EstrategiaBasica implements EstrategiaBusqueda {
    private final Pregunta[] orden = {
        Pregunta.COLORADO, Pregunta.NEGRO, Pregunta.AMARILLO,
        Pregunta.CALVICIE, Pregunta.LENTES, Pregunta.FEMENINO, Pregunta.MASCULINO
    };

    public boolean decidirSiAdivinar(List<Personaje> candidatos) {
        return candidatos.size() == 1;
    }

    public Pregunta elegirPregunta(List<Personaje> candidatos) {
        for (Pregunta pregunta : orden) {
            int si = 0;
            for (Personaje personaje : candidatos) {
                if (pregunta.coincide(personaje)) { si++; }
            }
            if (si > 0 && si < candidatos.size()) { return pregunta; }
        }
        return null;
    }

    public Personaje seleccionarPersonajeSospechoso(List<Personaje> candidatos) {
        if (candidatos.isEmpty()) { throw new IllegalStateException("No hay candidatos."); }
        return candidatos.get(0);
    }
}
