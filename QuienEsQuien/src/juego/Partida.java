package juego;

import estrategia.EstrategiaBasica;
import estrategia.EstrategiaGreedy;
import modelo.Humano;
import modelo.Jugador;
import modelo.Maquina;
import modelo.Personaje;
import modelo.Pregunta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Coordina los duelos. Las estrategias nunca reciben la partida ni al rival. */
public class Partida {
    private final boolean conHumano;
    private final CatalogoPersonajes catalogo;
    private final Random azar;
    private final Humano humano;
    private final Maquina maquina1;
    private final Maquina maquina2;
    private final EstrategiaGreedy estrategia2;
    private final String resumenSecretos;
    private final StringBuilder registro = new StringBuilder();
    private final List<Pregunta> preguntasDeMaquina1 = new ArrayList<Pregunta>();
    private final List<Boolean> respuestasDeMaquina1 = new ArrayList<Boolean>();
    private final List<Pregunta> preguntasHechasM1 = new ArrayList<Pregunta>();
    private final List<Pregunta> preguntasHechasM2 = new ArrayList<Pregunta>();
    private static final int ERROR_M1 = 30;
    private static final int ERROR_M2 = 40;
    private static final int PREGUNTAS_MINIMAS_ARRIESGO_M1 = 2;
    private static final int PROBABILIDAD_ARRIESGO_M1 = 50;
    private int etapa = 1;
    private boolean esperaSegundaEtapa;
    private int victoriasHumano;
    private int turnoActual;
    private int numeroTurno = 1;
    private int ganador = -1;

    public Partida(boolean conHumano, String nombre, Personaje elegido,
                   CatalogoPersonajes catalogo, Random azar) {
        this.conHumano = conHumano;
        this.catalogo = catalogo;
        this.azar = azar;
        List<Personaje> universo = catalogo.getPersonajes();
        List<Personaje> disponibles = new ArrayList<Personaje>(universo);
        if (conHumano) {
            if (nombre == null || nombre.trim().isEmpty() || elegido == null) {
                throw new IllegalArgumentException("Ingresá tu nombre y elegí un personaje.");
            }
            // El arbitro evita secretos repetidos; la estrategia no recibe el ID humano.
            elegido = catalogo.buscarPorId(elegido.getId());
            disponibles.remove(elegido);
            humano = new Humano(nombre.trim(), elegido, universo);
        } else {
            humano = null;
        }
        Personaje secreto1 = disponibles.remove(azar.nextInt(disponibles.size()));
        Personaje secreto2 = disponibles.remove(azar.nextInt(disponibles.size()));
        estrategia2 = new EstrategiaGreedy();
        maquina1 = new Maquina("Máquina 1", secreto1, universo, new EstrategiaBasica());
        maquina2 = new Maquina("Máquina 2", secreto2, universo, estrategia2);
        turnoActual = conHumano ? 0 : 1;
        resumenSecretos = (conHumano ? humano.getNombre() + ": " + elegido + "\n" : "")
                + "Máquina 1: " + secreto1 + "\nMáquina 2: " + secreto2;
        String inicio = conHumano ? "ETAPA 1 · Vos contra Máquina 1\nGanale para pasar a Máquina 2.\n\n"
                : "MÁQUINA CONTRA MÁQUINA\n\n";
        registro.append(inicio);
    }

    public void preguntarHumano(Pregunta pregunta) {
        comprobarTurnoHumano();
        if (pregunta == null) { throw new IllegalArgumentException("Seleccioná una pregunta."); }
        Maquina rival = getMaquina(etapa);
        Tablero tablero = humano.getTablero(etapa);
        preguntar(humano, rival, tablero, pregunta);
        siguienteTurno();
    }

    public void arriesgarHumano(int id) {
        comprobarTurnoHumano();
        Maquina rival = getMaquina(etapa);
        Personaje sospechoso = catalogo.buscarPorId(id);
        Tablero tablero = humano.getTablero(etapa);
        arriesgar(humano, rival, tablero, sospechoso);
        siguienteTurno();
    }

    public void jugarTurnoMaquina() {
        if (estaTerminada() || esperaSegundaEtapa || turnoActual == 0) {
            throw new IllegalStateException("Ahora no corresponde un turno de máquina.");
        }
        Maquina actual = getMaquina(turnoActual);
        Jugador rival = conHumano ? humano : getMaquina(turnoActual == 1 ? 2 : 1);
        Tablero tablero = actual.getTablero();

        List<Pregunta> historial = turnoActual == 1 ? preguntasHechasM1 : preguntasHechasM2;
        int probabilidadError = turnoActual == 1 ? ERROR_M1 : ERROR_M2;

        // Después de realizar al menos dos preguntas, M1 decide en cada turno si
        // arriesga o sigue investigando. Si arriesga, elige al azar entre los
        // candidatos restantes: con 2 candidatos tiene 50% de probabilidad de acertar.
        boolean puedeArriesgarM1 = turnoActual == 1
                && historial.size() >= PREGUNTAS_MINIMAS_ARRIESGO_M1
                && tablero.cantidad() > 1;
        boolean arriesgaPorProbabilidad = puedeArriesgarM1
                && azar.nextInt(100) < PROBABILIDAD_ARRIESGO_M1;

        if (arriesgaPorProbabilidad) {
            List<Personaje> candidatos = tablero.getCandidatos();
            Personaje sospechoso = candidatos.get(azar.nextInt(candidatos.size()));
            arriesgar(actual, rival, tablero, sospechoso);
            siguienteTurno();
            return;
        }

        Pregunta pregunta = null;
        boolean repitePorError = !historial.isEmpty() && azar.nextInt(100) < probabilidadError;
        if (repitePorError) {
            pregunta = historial.get(azar.nextInt(historial.size()));
        } else if (!actual.prefiereAdivinar()) {
            pregunta = actual.formularPregunta();
        }

        if (pregunta != null) {
            boolean respuesta = preguntar(actual, rival, tablero, pregunta);
            historial.add(pregunta);
            if (turnoActual == 1) { recordarPregunta(pregunta, respuesta); }
        } else {
            Personaje sospechoso = actual.arriesgarPersonaje();
            arriesgar(actual, rival, tablero, sospechoso);
        }
        siguienteTurno();
    }

    private void recordarPregunta(Pregunta pregunta, boolean respuesta) {
        if (conHumano) {
            // M2 no juega en esta etapa. Se almacena la informacion para el segundo duelo.
            preguntasDeMaquina1.add(pregunta);
            respuestasDeMaquina1.add(respuesta);
        } else {
            estrategia2.conocerPregunta(pregunta);
        }
    }

    public void iniciarSegundaEtapa() {
        if (!conHumano || estaTerminada() || !esperaSegundaEtapa || etapa != 1) {
            throw new IllegalStateException("Primero tenés que ganarle a Máquina 1.");
        }
        etapa = 2;
        esperaSegundaEtapa = false;
        turnoActual = 0;
        numeroTurno = 1;
        registro.append("\nETAPA 2 · Vos contra Máquina 2\n");
        Tablero tablero2 = maquina2.getTablero();
        for (int i = 0; i < preguntasDeMaquina1.size(); i++) {
            Pregunta pregunta = preguntasDeMaquina1.get(i);
            boolean respuesta = respuestasDeMaquina1.get(i);
            estrategia2.conocerPregunta(pregunta);
            preguntasHechasM2.add(pregunta);
            tablero2.filtrar(pregunta, respuesta);
        }
        registro.append("M2 recuerda ").append(preguntasDeMaquina1.size())
                .append(preguntasDeMaquina1.size() == 1 ? " pregunta de M1. Empieza con " : " preguntas de M1. Empieza con ")
                .append(tablero2.cantidad())
                .append(" candidatos.\nTu personaje sigue siendo el mismo.\n\n");
    }

    private boolean preguntar(Jugador actual, Jugador rival, Tablero tablero, Pregunta pregunta) {
        boolean respuesta = rival.evaluarPreguntaRival(pregunta);
        int antes = tablero.cantidad();
        tablero.filtrar(pregunta, respuesta);
        registro.append(numeroTurno).append(" · ").append(actual.getNombre()).append(": ")
                .append(pregunta).append(" ").append(respuesta ? "Sí" : "No").append(".\n")
                .append("Quedan ").append(tablero.cantidad()).append(" (antes ").append(antes).append(").\n\n");
        return respuesta;
    }

    private void arriesgar(Jugador actual, Jugador rival, Tablero tablero, Personaje sospechoso) {
        registro.append(numeroTurno).append(" · ").append(actual.getNombre())
                .append(" arriesga: ").append(sospechoso.getNombre()).append(".\n");
        if (rival.esMiPersonajeSecreto(sospechoso)) {
            if (conHumano && turnoActual == 0) { victoriasHumano++; }
            if (conHumano && turnoActual == 0 && etapa == 1) {
                esperaSegundaEtapa = true;
                registro.append("¡Ganaste a Máquina 1! Ya podés pasar a la segunda etapa.\n\n");
            } else {
                ganador = turnoActual;
                registro.append("¡Ganó ").append(actual.getNombre()).append("!")
                        .append(ganoHumano() ? " Superaste las dos etapas.\n" : "\n")
                        .append(resumenSecretos).append("\n");
            }
        } else {
            tablero.descartar(sospechoso.getId());
            registro.append("No acertó. Quedan ").append(tablero.cantidad()).append(" candidatos.\n\n");
        }
    }

    private void siguienteTurno() {
        if (!estaTerminada() && !esperaSegundaEtapa) {
            numeroTurno++;
            // Solo alternan los dos participantes del duelo actual.
            if (conHumano) { turnoActual = turnoActual == 0 ? etapa : 0; }
            else { turnoActual = turnoActual == 1 ? 2 : 1; }
        }
    }

    private void comprobarTurnoHumano() {
        if (!conHumano || estaTerminada() || esperaSegundaEtapa || turnoActual != 0) {
            throw new IllegalStateException("Ahora no corresponde un turno humano.");
        }
    }

    private Maquina getMaquina(int numero) {
        if (numero == 1) { return maquina1; }
        if (numero == 2) { return maquina2; }
        throw new IllegalArgumentException("La máquina debe ser 1 o 2.");
    }

    /** Las vistas 0 y 1 corresponden a los dos jugadores del duelo actual. */
    public Tablero getTablero(int vista) {
        if (vista != 0 && vista != 1) { throw new IllegalArgumentException("No existe ese tablero."); }
        if (conHumano) { return vista == 0 ? humano.getTablero(etapa) : getMaquina(etapa).getTablero(); }
        return getMaquina(vista + 1).getTablero();
    }

    public boolean esConHumano() { return conHumano; }
    public boolean estaTerminada() { return ganador != -1; }
    public boolean ganoHumano() { return ganador == 0; }
    public boolean esperaSegundaEtapa() { return esperaSegundaEtapa; }
    public int getEtapa() { return etapa; }
    public int getVictoriasHumano() { return victoriasHumano; }
    public int getTurnoActual() { return turnoActual; }
    public int getNumeroTurno() { return numeroTurno; }
    public String getRegistro() { return registro.toString(); }
    public String getNombreHumano() { return conHumano ? humano.getNombre() : ""; }

    public String getNombreTurno() {
        return turnoActual == 0 ? humano.getNombre() : getMaquina(turnoActual).getNombre();
    }

    public String getNombreGanador() {
        if (!estaTerminada()) { return ""; }
        return ganador == 0 ? humano.getNombre() : getMaquina(ganador).getNombre();
    }

    public String getResumenFinal() {
        if (!estaTerminada()) { return "Los secretos se muestran al finalizar el desafío."; }
        return resumenSecretos;
    }
}
