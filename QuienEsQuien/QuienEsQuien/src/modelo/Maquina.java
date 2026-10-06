package modelo;

import estrategia.EstrategiaBusqueda;
import juego.Tablero;

import java.util.List;

public class Maquina extends Jugador {
    private final Tablero tablero;
    private final EstrategiaBusqueda estrategia;

    public Maquina(String nombre, Personaje secreto, List<Personaje> universo,
                   EstrategiaBusqueda estrategia) {
        super(nombre, secreto);
        tablero = new Tablero(universo);
        this.estrategia = estrategia;
    }

    public Tablero getTablero() { return tablero; }
    public boolean prefiereAdivinar() {
        return estrategia.decidirSiAdivinar(tablero.getCandidatos());
    }
    public Pregunta formularPregunta() {
        return estrategia.elegirPregunta(tablero.getCandidatos());
    }
    public Personaje arriesgarPersonaje() {
        return estrategia.seleccionarPersonajeSospechoso(tablero.getCandidatos());
    }
}
