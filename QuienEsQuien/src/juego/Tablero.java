package juego;

import modelo.Personaje;
import modelo.Pregunta;

import java.util.ArrayList;
import java.util.List;

/** Cada busqueda tiene su propia lista, aunque todas empiezan con las mismas fichas. */
public class Tablero {
    private List<Personaje> candidatos;

    public Tablero(List<Personaje> universo) {
        candidatos = new ArrayList<Personaje>(universo);
    }

    public void filtrar(Pregunta pregunta, boolean respuesta) {
        List<Personaje> restantes = new ArrayList<Personaje>();
        for (Personaje personaje : candidatos) {
            if (pregunta.coincide(personaje) == respuesta) {
                restantes.add(personaje);
            }
        }
        candidatos = restantes;
    }

    public void descartar(int id) {
        for (int i = 0; i < candidatos.size(); i++) {
            if (candidatos.get(i).getId() == id) {
                candidatos.remove(i);
                return;
            }
        }
    }

    public int cantidad() { return candidatos.size(); }

    public List<Personaje> getCandidatos() {
        return new ArrayList<Personaje>(candidatos);
    }

    public boolean contiene(int id) {
        for (Personaje personaje : candidatos) {
            if (personaje.getId() == id) { return true; }
        }
        return false;
    }

}
