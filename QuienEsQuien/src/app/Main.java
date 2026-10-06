package app;

import juego.CatalogoPersonajes;
import persistencia.Marcador;
import ui.VentanaJuego;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("No se puede abrir la ventana: Java se inicio sin entorno grafico.");
            System.err.println("Ejecuta Main desde IntelliJ con un JDK configurado.");
            System.err.println("Si configuraste -Djava.awt.headless=true, quita esa opcion de ejecucion.");
            System.exit(1);
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
                catch (Exception e) { /* El estilo predeterminado tambien permite jugar. */ }
                try {
                    CatalogoPersonajes catalogo = new CatalogoPersonajes();
                    // Ruta estable aunque se ejecute desde IntelliJ o desde el JAR.
                    File archivo = new File(System.getProperty("user.home"), "QuienEsQuien/marcador.csv");
                    VentanaJuego panel = new VentanaJuego(catalogo, new Marcador(archivo), new Random());
                    JFrame ventana = new JFrame("Quién es quién - Programación III");
                    ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    ventana.setContentPane(panel);
                    Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
                    int ancho = Math.min(1360, pantalla.width - 40);
                    int alto = Math.min(880, pantalla.height - 70);
                    ventana.setMinimumSize(new Dimension(Math.min(1060, ancho), Math.min(720, alto)));
                    ventana.setSize(ancho, alto);
                    ventana.setLocationRelativeTo(null);
                    ventana.setVisible(true);
                } catch (Exception e) {
                    // Un error al crear la ventana debe mostrarse y marcar el inicio como fallido.
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "No se pudo abrir el juego.\n" + e.toString(),
                            "Error al iniciar", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });
    }
}
