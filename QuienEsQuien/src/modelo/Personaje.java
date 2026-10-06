package modelo;

/** Los datos de una ficha no cambian durante el juego. */
public class Personaje {
    private final int id;
    private final String nombre;
    private final Genero genero;
    private final boolean calvicie;
    private final boolean lentes;
    private final ColorPelo colorPelo;

    public Personaje(int id, String nombre, Genero genero, boolean calvicie,
                     boolean lentes, ColorPelo colorPelo) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.calvicie = calvicie;
        this.lentes = lentes;
        this.colorPelo = colorPelo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Genero getGenero() { return genero; }
    public boolean tieneCalvicie() { return calvicie; }
    public boolean tieneLentes() { return lentes; }
    public ColorPelo getColorPelo() { return colorPelo; }

    public String toString() { return id + " - " + nombre; }
}
