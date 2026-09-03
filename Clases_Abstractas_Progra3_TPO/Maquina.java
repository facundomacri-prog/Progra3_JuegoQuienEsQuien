import Personaje;
import java.util.List;

public abstract class Maquina extends Jugador {

    public Maquina(String nombre, Personaje personajeSecreto, List<Personaje> universoPersonajes) {
        super(nombre, personajeSecreto, universoPersonajes);
    }

    // Hereda los métodos abstractos de Jugador.

    //Basicamente un wait/sleep de x tiempo, para que no te tire todos los mensajes de una
    public abstract void simularTiempoPensamiento();

    //en base a lo que paso o va a pasar, te va a dar un dialogo.
    public abstract String generarDialogo(TipoEvento evento);


}