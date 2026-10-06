package juego;

import modelo.ColorPelo;
import modelo.Genero;
import modelo.Personaje;

import java.util.ArrayList;
import java.util.List;
public class CatalogoPersonajes {

    private final List<Personaje> personajes = new ArrayList<Personaje>();
    private int siguienteId = 1;

    public CatalogoPersonajes() {

        agregar("Lucia", Genero.FEMENINO, false, false, ColorPelo.COLORADO);
        agregar("Ana", Genero.FEMENINO, false, true, ColorPelo.COLORADO);
        agregar("Julia", Genero.FEMENINO, true, false, ColorPelo.COLORADO);
        agregar("Carla", Genero.FEMENINO, true, true, ColorPelo.COLORADO);
        agregar("Ines", Genero.FEMENINO, false, false, ColorPelo.NEGRO);
        agregar("Beatriz", Genero.FEMENINO, false, true, ColorPelo.NEGRO);
        agregar("Laura", Genero.FEMENINO, true, false, ColorPelo.NEGRO);
        agregar("Diana", Genero.FEMENINO, true, true, ColorPelo.NEGRO);
        agregar("Gabriela", Genero.FEMENINO, false, false, ColorPelo.AMARILLO);
        agregar("Elena", Genero.FEMENINO, false, true, ColorPelo.AMARILLO);
        agregar("Helena", Genero.FEMENINO, true, false, ColorPelo.AMARILLO);
        agregar("Florencia", Genero.FEMENINO, true, true, ColorPelo.AMARILLO);

        agregar("Tomas", Genero.MASCULINO, false, false, ColorPelo.COLORADO);
        agregar("Bruno", Genero.MASCULINO, false, true, ColorPelo.COLORADO);
        agregar("Sergio", Genero.MASCULINO, true, false, ColorPelo.COLORADO);
        agregar("Diego", Genero.MASCULINO, true, true, ColorPelo.COLORADO);
        agregar("Rafael", Genero.MASCULINO, false, false, ColorPelo.NEGRO);
        agregar("Andres", Genero.MASCULINO, false, true, ColorPelo.NEGRO);
        agregar("Pablo", Genero.MASCULINO, true, false, ColorPelo.NEGRO);
        agregar("Esteban", Genero.MASCULINO, true, true, ColorPelo.NEGRO);
        agregar("Nicolas", Genero.MASCULINO, false, false, ColorPelo.AMARILLO);
        agregar("Hugo", Genero.MASCULINO, false, true, ColorPelo.AMARILLO);
        agregar("Mateo", Genero.MASCULINO, true, false, ColorPelo.AMARILLO);

        // Ordenamiento inicial mediante MergeSort
        List<Personaje> ordenados = mergeSort(personajes);

        personajes.clear();
        personajes.addAll(ordenados);
    }

    private void agregar(String nombre,
                         Genero genero,
                         boolean calvicie,
                         boolean lentes,
                         ColorPelo color) {

        personajes.add(
                new Personaje(
                        siguienteId++,
                        nombre,
                        genero,
                        calvicie,
                        lentes,
                        color
                )
        );
    }

    private List<Personaje> mergeSort(List<Personaje> lista) {

        // CASO BASE
        if (lista.size() <= 1) {
            return new ArrayList<Personaje>(lista);
        }

        // DIVIDIR
        int medio = lista.size() / 2;

        List<Personaje> izquierda =
                new ArrayList<Personaje>(
                        lista.subList(0, medio)
                );

        List<Personaje> derecha =
                new ArrayList<Personaje>(
                        lista.subList(medio, lista.size())
                );

        // CONQUISTAR
        izquierda = mergeSort(izquierda);
        derecha = mergeSort(derecha);

        // COMBINAR
        return mezclar(izquierda, derecha);
    }

    private List<Personaje> mezclar(
            List<Personaje> izquierda,
            List<Personaje> derecha) {

        List<Personaje> resultado =
                new ArrayList<Personaje>();

        int i = 0;
        int j = 0;

        while (i < izquierda.size()
                && j < derecha.size()) {

            Personaje personajeIzquierda =
                    izquierda.get(i);

            Personaje personajeDerecha =
                    derecha.get(j);

            if (vaAntes(personajeIzquierda,
                    personajeDerecha)) {

                resultado.add(personajeIzquierda);
                i++;

            } else {

                resultado.add(personajeDerecha);
                j++;
            }
        }

        // Agregar elementos restantes de la izquierda
        while (i < izquierda.size()) {
            resultado.add(izquierda.get(i));
            i++;
        }

        // Agregar elementos restantes de la derecha
        while (j < derecha.size()) {
            resultado.add(derecha.get(j));
            j++;
        }

        return resultado;
    }

    private boolean vaAntes(Personaje a,
                            Personaje b) {

        if (a.getGenero() == b.getGenero()) {
            return a.getId() <= b.getId();
        }

        return a.getGenero() == Genero.FEMENINO;
    }

    public List<Personaje> getPersonajes() {
        return new ArrayList<Personaje>(personajes);
    }

    public Personaje buscarPorId(int id) {

        for (Personaje personaje : personajes) {

            if (personaje.getId() == id) {
                return personaje;
            }
        }

        throw new IllegalArgumentException(
                "No existe el personaje con ID " + id
        );
    }
}