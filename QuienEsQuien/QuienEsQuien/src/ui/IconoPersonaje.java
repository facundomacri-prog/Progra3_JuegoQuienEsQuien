package ui;

import modelo.ColorPelo;
import modelo.Genero;
import modelo.Personaje;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/** Dibujo sencillo con Swing: no necesita imagenes ni librerias externas. */
public class IconoPersonaje implements Icon {
    private final Personaje personaje;

    public IconoPersonaje(Personaje personaje) { this.personaje = personaje; }
    public int getIconWidth() { return 48; }
    public int getIconHeight() { return 60; }

    public void paintIcon(Component componente, Graphics original, int x, int y) {
        Graphics g = original.create();
        g.translate(x, y);
        g.setColor(personaje.getGenero() == Genero.FEMENINO
                ? new Color(110, 84, 160) : new Color(44, 115, 156));
        g.fillRoundRect(6, 43, 36, 17, 14, 14);
        g.setColor(new Color(237, 190, 145));
        g.fillOval(7, 7, 34, 40);
        Color pelo = Color.BLACK;
        if (personaje.getColorPelo() == ColorPelo.COLORADO) { pelo = new Color(177, 69, 37); }
        if (personaje.getColorPelo() == ColorPelo.AMARILLO) { pelo = new Color(223, 168, 24); }
        g.setColor(pelo);
        g.fillRect(5, 15, 5, 17);
        g.fillRect(38, 15, 5, 17);
        if (!personaje.tieneCalvicie()) { g.fillArc(6, 4, 36, 29, 0, 180); }
        g.setColor(new Color(36, 41, 49));
        g.fillOval(16, 24, 3, 3);
        g.fillOval(29, 24, 3, 3);
        if (personaje.tieneLentes()) {
            g.drawRect(11, 20, 12, 10);
            g.drawRect(25, 20, 12, 10);
            g.drawLine(23, 24, 25, 24);
        }
        g.drawArc(18, 31, 13, 9, 180, 180);
        g.dispose();
    }
}
