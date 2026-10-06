package ui;

import juego.CatalogoPersonajes;
import juego.Partida;
import juego.Tablero;
import modelo.Personaje;
import modelo.Pregunta;
import persistencia.Marcador;
import persistencia.Puntaje;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/** La vista envia acciones a Partida y muestra su estado. */
public class VentanaJuego extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final CatalogoPersonajes catalogo;
    private final Marcador marcador;
    private final Random azar;
    private Partida partida;
    private int victoriasProcesadas;
    private final Color azul = new Color(27, 64, 94);
    private final JTextField nombre = new JTextField("", 12);
    private final JComboBox<String> modo = new JComboBox<String>(new String[] {
        "Humano vs maquinas", "Máquina contra máquina"
    });
    private final JComboBox<Personaje> secreto = new JComboBox<Personaje>();
    private final JButton comenzar = new JButton("Comenzar");
    private final JButton nueva = new JButton("Nueva partida");
    private final JButton records = new JButton("Récords");
    private final JButton continuar = new JButton("Continuar contra Máquina 2");
    private final JComboBox<String> vista = new JComboBox<String>();
    private final JLabel estado = new JLabel("Elegí un modo, tu nombre y tu personaje.");
    private final JLabel estadoSecundario = new JLabel(" ");
    private final JLabel conteo = new JLabel("23 personajes");
    private final JPanel fichas = new JPanel(new GridLayout(0, 4, 6, 6));
    private final JTextArea proceso = new JTextArea();
    private final JLabel rivalActual = new JLabel("Rival: Máquina 1");
    private final JComboBox<Pregunta> pregunta = new JComboBox<Pregunta>(Pregunta.values());
    private final JComboBox<Personaje> sospechoso = new JComboBox<Personaje>();
    private final JButton preguntar = new JButton("Preguntar");
    private final JButton arriesgar = new JButton("Arriesgar personaje");
    private final JButton siguiente = new JButton("Siguiente turno");
    private final JButton automatico = new JButton("Reproducir");
    private final Timer temporizador;

    public VentanaJuego(CatalogoPersonajes catalogo, Marcador marcador, Random azar) {
        this.catalogo = catalogo;
        this.marcador = marcador;
        this.azar = azar;
        temporizador = new Timer(800, this);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));
        setBackground(new Color(241, 245, 249));
        prepararCabecera();
        prepararCentro();
        prepararAcciones();
        for (Personaje personaje : catalogo.getPersonajes()) { secreto.addItem(personaje); }
        vista.addItem("Catálogo completo · agrupado por género e ID");
        nombre.setName("nombre"); modo.setName("modo"); secreto.setName("secreto");
        comenzar.setName("comenzar"); nueva.setName("nueva"); vista.setName("vista");
        continuar.setName("continuar"); pregunta.setName("pregunta"); sospechoso.setName("sospechoso");
        preguntar.setName("preguntar"); arriesgar.setName("arriesgar");
        siguiente.setName("siguiente"); automatico.setName("automatico");
        proceso.setName("proceso"); estado.setName("estado");
        JButton[] botones = {comenzar, nueva, records, continuar, preguntar, arriesgar, siguiente, automatico};
        for (JButton boton : botones) { boton.addActionListener(this); }
        modo.addActionListener(this); secreto.addActionListener(this);
        vista.addActionListener(this);
        proceso.setText("CÓMO JUGAR\n\nPrimero competís contra Máquina 1. Si ganás, pasás a Máquina 2.\n\n"
                + "Tu personaje se mantiene entre etapas y Máquina 2 recuerda las preguntas de la primera.\n\n"
                + "Preguntá o arriesgá en tu turno. Cada duelo ganado suma una victoria al récord.\n\n"
                + "Máquina 1, después de hacer 2 preguntas, puede decidir arriesgar un personaje o seguir preguntando.\n\n"
                + "En máquina contra máquina, usá Siguiente turno o Reproducir.\n\n"
                + "El resumen muestra las jugadas.");
        actualizar();
    }

    private void prepararCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(0, 9));
        cabecera.setOpaque(false);
        JPanel titulos = new JPanel(new GridLayout(0, 1, 0, 3));
        titulos.setOpaque(false);
        JLabel titulo = new JLabel("¿QUIÉN ES QUIÉN?");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(azul);
        titulos.add(titulo);
        JLabel subtitulo = new JLabel("23 personajes · 2 estrategias · cada pregunta cuenta");
        subtitulo.setForeground(new Color(78, 95, 112));
        titulos.add(subtitulo);
        cabecera.add(titulos, BorderLayout.NORTH);
        JPanel configuracion = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        configuracion.setOpaque(false);
        configuracion.add(new JLabel("Modo")); configuracion.add(modo);
        configuracion.add(new JLabel("Nombre")); configuracion.add(nombre);
        configuracion.add(new JLabel("Tu secreto")); configuracion.add(secreto);
        configuracion.add(comenzar); configuracion.add(nueva);
        cabecera.add(configuracion, BorderLayout.CENTER);
        JPanel informacion = new JPanel(new GridLayout(0, 1, 0, 3));
        informacion.setOpaque(false);
        estado.setFont(new Font("SansSerif", Font.BOLD, 15)); estado.setForeground(azul);
        informacion.add(estado); informacion.add(estadoSecundario);
        cabecera.add(informacion, BorderLayout.SOUTH);
        add(cabecera, BorderLayout.NORTH);
    }

    private void prepararCentro() {
        JPanel tablero = new JPanel(new BorderLayout(5, 7));
        tablero.setOpaque(false);
        JPanel controles = new JPanel(new BorderLayout(8, 0));
        controles.setOpaque(false);
        controles.add(vista, BorderLayout.CENTER); controles.add(conteo, BorderLayout.EAST);
        tablero.add(controles, BorderLayout.NORTH);
        fichas.setBackground(new Color(241, 245, 249));
        fichas.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        JScrollPane scrollFichas = new JScrollPane(fichas);
        scrollFichas.getVerticalScrollBar().setUnitIncrement(24);
        tablero.add(scrollFichas, BorderLayout.CENTER);
        JLabel leyenda = new JLabel("Rojo = descartado. Tocá una ficha para seleccionarla.");
        leyenda.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablero.add(leyenda, BorderLayout.SOUTH);
        JPanel lateral = new JPanel(new BorderLayout(0, 7));
        lateral.setOpaque(false);
        JLabel titulo = new JLabel("RESUMEN DE LA PARTIDA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(azul);
        lateral.add(titulo, BorderLayout.NORTH);
        proceso.setEditable(false); proceso.setLineWrap(true); proceso.setWrapStyleWord(true);
        proceso.setFont(new Font("SansSerif", Font.PLAIN, 13));
        proceso.setMargin(new java.awt.Insets(12, 12, 12, 12));
        lateral.add(new JScrollPane(proceso), BorderLayout.CENTER);
        JPanel herramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        herramientas.setOpaque(false); herramientas.add(records);
        lateral.add(herramientas, BorderLayout.SOUTH);
        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablero, lateral);
        division.setResizeWeight(0.64); division.setDividerLocation(810);
        tablero.setMinimumSize(new Dimension(570, 250));
        lateral.setMinimumSize(new Dimension(400, 250));
        division.setBorder(null);
        add(division, BorderLayout.CENTER);
    }

    private void prepararAcciones() {
        JPanel acciones = new JPanel(new GridLayout(0, 1, 0, 2));
        acciones.setOpaque(false);
        JPanel linea1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        linea1.setOpaque(false);
        linea1.add(rivalActual);
        linea1.add(pregunta); linea1.add(preguntar);
        JPanel linea2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        linea2.setOpaque(false);
        linea2.add(new JLabel("Mi sospechoso")); linea2.add(sospechoso); linea2.add(arriesgar);
        linea2.add(siguiente); linea2.add(automatico); linea2.add(continuar);
        acciones.add(linea1); acciones.add(linea2);
        add(acciones, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent evento) {
        Object origen = evento.getSource();
        try {
            if (origen == comenzar) {
                boolean conHumano = modo.getSelectedIndex() == 0;
                if (conHumano) { Marcador.validarNombre(nombre.getText()); }
                partida = new Partida(conHumano, nombre.getText(), (Personaje) secreto.getSelectedItem(), catalogo, azar);
                victoriasProcesadas = 0;
                configurarVistas();
            } else if (origen == continuar) {
                partida.iniciarSegundaEtapa();
                configurarVistas();
            } else if (origen == nueva) {
                temporizador.stop(); partida = null; vista.removeAllItems();
                vista.addItem("Catálogo completo · agrupado por género e ID");
                proceso.setText("Nueva partida. Elegí el modo y tu personaje antes de comenzar.");
            } else if (origen == preguntar) {
                partida.preguntarHumano((Pregunta) pregunta.getSelectedItem());
                temporizador.start();
            } else if (origen == arriesgar) {
                Personaje elegido = (Personaje) sospechoso.getSelectedItem();
                if (elegido == null) { return; }
                partida.arriesgarHumano(elegido.getId());
                temporizador.start();
            } else if (origen == siguiente || origen == temporizador) {
                if (origen == siguiente) { temporizador.stop(); }
                if (partida != null && !partida.estaTerminada() && !partida.esperaSegundaEtapa() && partida.getTurnoActual() != 0) {
                    partida.jugarTurnoMaquina();
                }
            } else if (origen == automatico) {
                if (temporizador.isRunning()) { temporizador.stop(); }
                else { temporizador.start(); }
            } else if (origen == records) {
                mostrarRecords(); return;
            } else if (evento.getActionCommand() != null && evento.getActionCommand().startsWith("ficha:")) {
                int id = Integer.parseInt(evento.getActionCommand().substring(6));
                Personaje elegido = catalogo.buscarPorId(id);
                if (partida == null) { secreto.setSelectedItem(elegido); }
                else { sospechoso.setSelectedItem(elegido); }
            }
            actualizar();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Revisá los datos", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException e) {
            temporizador.stop();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Turno inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void configurarVistas() {
        vista.removeAllItems();
        if (partida.esConHumano()) {
            vista.addItem("Mis candidatos para Máquina " + partida.getEtapa());
            vista.addItem("Candidatos de Máquina " + partida.getEtapa());
        } else {
            vista.addItem("Candidatos de Máquina 1");
            vista.addItem("Candidatos de Máquina 2");
        }
        vista.setSelectedIndex(0);
    }

    private void actualizar() {
        boolean enPartida = partida != null;
        boolean conHumano = enPartida ? partida.esConHumano() : modo.getSelectedIndex() == 0;
        boolean fin = enPartida && partida.estaTerminada();
        boolean espera = enPartida && partida.esperaSegundaEtapa();
        boolean turnoHumano = enPartida && !fin && !espera && partida.getTurnoActual() == 0;
        boolean turnoMaquina = enPartida && !fin && !espera && !turnoHumano;
        if (!turnoMaquina) { temporizador.stop(); }
        modo.setEnabled(!enPartida); nombre.setEnabled(!enPartida && conHumano);
        secreto.setEnabled(!enPartida && conHumano); comenzar.setEnabled(!enPartida);
        nueva.setEnabled(enPartida); vista.setEnabled(enPartida);
        rivalActual.setText(enPartida && conHumano ? "Rival: Máquina " + partida.getEtapa() : "Tu jugada");
        pregunta.setEnabled(turnoHumano);
        preguntar.setEnabled(turnoHumano); sospechoso.setEnabled(turnoHumano); arriesgar.setEnabled(turnoHumano);
        siguiente.setEnabled(turnoMaquina); automatico.setEnabled(turnoMaquina);
        continuar.setVisible(enPartida && conHumano); continuar.setEnabled(espera);
        automatico.setText(temporizador.isRunning() ? "Pausar" : "Reproducir");
        if (enPartida) {
            if (espera) { estado.setText("¡Ganaste a Máquina 1! Continuá contra Máquina 2."); }
            else if (fin) {
                estado.setText(partida.ganoHumano() ? "¡Ganaste las dos etapas!"
                        : "Ganó " + partida.getNombreGanador() + " · juego terminado");
            } else {
                estado.setText((conHumano ? "Etapa " + partida.getEtapa() + " de 2 · " : "")
                        + "Turno " + partida.getNumeroTurno() + " · juega " + partida.getNombreTurno());
            }
            estadoSecundario.setText(conHumano ? "Tu secreto fijo: " + secreto.getSelectedItem()
                    + "  |  Duelos ganados: " + partida.getVictoriasHumano()
                    : "Cada jugada se resume en dos líneas.");
            String texto = partida.getRegistro();
            if (!texto.equals(proceso.getText())) {
                proceso.setText(texto); proceso.setCaretPosition(proceso.getDocument().getLength());
            }
        } else {
            estado.setText(conHumano ? "Primero contra Máquina 1. Ganá para pasar a Máquina 2."
                    : "Presioná Comenzar para observar a las dos máquinas.");
            estadoSecundario.setText("Orden de alta: femenino (ID 1-12) · masculino (ID 13-23).");
        }
        actualizarFichas();
        actualizarSospechosos();
        if (enPartida && partida.getVictoriasHumano() > victoriasProcesadas) {
            int nuevasVictorias = partida.getVictoriasHumano() - victoriasProcesadas;
            victoriasProcesadas = partida.getVictoriasHumano();
            try {
                for (int i = 0; i < nuevasVictorias; i++) {
                    marcador.registrarVictoria(partida.getNombreHumano());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Ganaste, pero no se pudo guardar el récord:\n" + e.getMessage(),
                        "No se guardó el resultado", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarFichas() {
        Tablero tablero = null;
        int indice = vista.getSelectedIndex();
        if (partida != null && indice >= 0) {
            tablero = partida.getTablero(indice);
        }
        conteo.setText((tablero == null ? 23 : tablero.cantidad()) + " / 23 candidatos");
        fichas.removeAll();
        boolean elegirSecreto = partida == null && modo.getSelectedIndex() == 0;
        boolean elegirSospechoso = partida != null && partida.esConHumano()
                && !partida.estaTerminada() && !partida.esperaSegundaEtapa()
                && partida.getTurnoActual() == 0 && indice == 0;
        for (Personaje personaje : catalogo.getPersonajes()) {
            boolean activo = tablero == null || tablero.contiene(personaje.getId());
            JButton ficha = new JButton("<html><b>" + personaje + "</b><br>"
                    + personaje.getGenero() + " · " + personaje.getColorPelo()
                    + "<br>Calvicie: " + (personaje.tieneCalvicie() ? "Sí" : "No")
                    + "<br>Lentes: " + (personaje.tieneLentes() ? "Sí" : "No") + "</html>", new IconoPersonaje(personaje));
            ficha.setFont(new Font("SansSerif", Font.PLAIN, 11));
            ficha.setHorizontalAlignment(SwingConstants.LEFT);
            ficha.setPreferredSize(new Dimension(192, 76));
            ficha.setMargin(new java.awt.Insets(5, 5, 5, 3));
            ficha.setBackground(activo ? Color.WHITE : new Color(255, 205, 210));
            ficha.setForeground(activo ? new Color(27, 42, 59) : new Color(145, 20, 30));
            ficha.setOpaque(true);
            ficha.setBorder(BorderFactory.createLineBorder(activo ? new Color(203, 212, 222) : new Color(205, 70, 80), 1));
            if (elegirSecreto && secreto.getSelectedItem() == personaje) {
                ficha.setBorder(BorderFactory.createLineBorder(azul, 3));
            }
            if (activo && (elegirSecreto || elegirSospechoso)) {
                ficha.setActionCommand("ficha:" + personaje.getId()); ficha.addActionListener(this);
            }
            fichas.add(ficha);
        }
        fichas.revalidate(); fichas.repaint();
    }

    private void actualizarSospechosos() {
        Personaje anterior = (Personaje) sospechoso.getSelectedItem();
        sospechoso.removeAllItems();
        if (partida != null && partida.esConHumano()) {
            for (Personaje personaje : partida.getTablero(0).getCandidatos()) {
                sospechoso.addItem(personaje);
            }
        }
        if (anterior != null) {
            for (int i = 0; i < sospechoso.getItemCount(); i++) {
                if (sospechoso.getItemAt(i).getId() == anterior.getId()) {
                    sospechoso.setSelectedIndex(i); break;
                }
            }
        }
    }

    private void mostrarRecords() {
        try {
            List<Puntaje> puntajes = marcador.cargar();
            JPanel contenido = new JPanel(new BorderLayout(0, 10));
            JPanel tabla = new JPanel(new GridLayout(0, 2, 18, 7));

            JLabel tituloNombre = new JLabel("NOMBRE", SwingConstants.CENTER);
            JLabel tituloVictorias = new JLabel("VICTORIAS", SwingConstants.CENTER);
            tituloNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
            tituloVictorias.setFont(new Font("SansSerif", Font.BOLD, 13));
            tabla.add(tituloNombre);
            tabla.add(tituloVictorias);

            if (puntajes.isEmpty()) {
                tabla.add(new JLabel("Todavía no hay victorias registradas.", SwingConstants.CENTER));
                tabla.add(new JLabel("0", SwingConstants.CENTER));
            } else {
                for (Puntaje puntaje : puntajes) {
                    JLabel nombreJugador = new JLabel(puntaje.getNombre(), SwingConstants.CENTER);
                    JLabel numeroVictorias = new JLabel(String.valueOf(puntaje.getVictorias()), SwingConstants.CENTER);
                    numeroVictorias.setFont(new Font("SansSerif", Font.BOLD, 16));
                    tabla.add(nombreJugador);
                    tabla.add(numeroVictorias);
                }
            }

            contenido.add(tabla, BorderLayout.CENTER);
            JLabel ayuda = new JLabel("Cada duelo ganado suma una victoria · " + marcador.getRuta(), SwingConstants.CENTER);
            ayuda.setFont(new Font("SansSerif", Font.PLAIN, 11));
            contenido.add(ayuda, BorderLayout.SOUTH);
            JOptionPane.showMessageDialog(this, contenido, "Récords", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo leer el marcador", JOptionPane.ERROR_MESSAGE);
        }
    }

}
