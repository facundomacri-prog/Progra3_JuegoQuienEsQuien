package estrategia;

import modelo.Personaje;
import modelo.Pregunta;

import java.util.List;

/** Elige la pregunta que deja los grupos Si y No mas equilibrados. */
public class EstrategiaGreedy implements EstrategiaBusqueda {
    private final boolean[] conocidas = new boolean[Pregunta.values().length];

    public void conocerPregunta(Pregunta pregunta) {
        conocidas[pregunta.ordinal()] = true;
    }

    public boolean decidirSiAdivinar(List<Personaje> candidatos) {
        return candidatos.size() == 1;
    }

    public Pregunta elegirPregunta(List<Personaje> candidatos) {
        Pregunta mejor = null;
        int mejorDiferencia = Integer.MAX_VALUE;
        for (Pregunta pregunta : Pregunta.values()) {
            int si = 0;
            for (Personaje personaje : candidatos) {
                if (pregunta.coincide(personaje)) { si++; }
            }
            int no = candidatos.size() - si;
            int diferencia = Math.abs(si - no);
            if (si == 0 || no == 0) { continue; }
            boolean empateConVentaja = mejor != null && diferencia == mejorDiferencia
                    && conocidas[pregunta.ordinal()] && !conocidas[mejor.ordinal()];
            if (diferencia < mejorDiferencia || empateConVentaja) {
                mejor = pregunta;
                mejorDiferencia = diferencia;
            }
        }
        return mejor;
    }

    public Personaje seleccionarPersonajeSospechoso(List<Personaje> candidatos) {
        if (candidatos.isEmpty()) { throw new IllegalStateException("No hay candidatos."); }
        return candidatos.get(0);
    }
}
