package modelo;

import juego.Tablero;

import java.util.List;

public class Humano extends Jugador {
    private final Tablero contraMaquina1;
    private final Tablero contraMaquina2;

    public Humano(String nombre, Personaje secreto, List<Personaje> universo) {
        super(nombre, secreto);
        contraMaquina1 = new Tablero(universo);
        contraMaquina2 = new Tablero(universo);
    }

    public Tablero getTablero(int rival) {
        if (rival == 1) { return contraMaquina1; }
        if (rival == 2) { return contraMaquina2; }
        throw new IllegalArgumentException("El rival debe ser 1 o 2.");
    }
}
