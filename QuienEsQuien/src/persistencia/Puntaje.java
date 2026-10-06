package persistencia;

public class Puntaje {
    private final String nombre;
    private int victorias;

    public Puntaje(String nombre, int victorias) {
        this.nombre = nombre;
        this.victorias = victorias;
    }

    public String getNombre() { return nombre; }
    public int getVictorias() { return victorias; }
    public void sumarVictoria() { victorias++; }
}
