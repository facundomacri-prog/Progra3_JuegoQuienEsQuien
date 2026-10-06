package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Persistencia en texto UTF-8. Solo guarda victorias de personas. */
public class Marcador {
    private final File archivo;

    public Marcador(File archivo) { this.archivo = archivo; }

    public static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.trim().length() > 30
                || nombre.indexOf(';') >= 0 || nombre.indexOf('\n') >= 0 || nombre.indexOf('\r') >= 0) {
            throw new IllegalArgumentException("Usá un nombre de 1 a 30 caracteres, sin punto y coma ni saltos de línea.");
        }
    }

    public List<Puntaje> cargar() throws IOException {
        List<Puntaje> puntajes = new ArrayList<Puntaje>();
        if (!archivo.exists()) { return puntajes; }
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(
                new FileInputStream(archivo), "UTF-8"))) {
            if (!"nombre;victorias".equals(lector.readLine())) {
                throw new IOException("El encabezado del marcador no es válido. Se conserva el archivo.");
            }
            String linea;
            while ((linea = lector.readLine()) != null) {
                int separador = linea.lastIndexOf(';');
                try {
                    if (separador < 1) { throw new IllegalArgumentException(); }
                    String nombre = linea.substring(0, separador);
                    validarNombre(nombre);
                    int victorias = Integer.parseInt(linea.substring(separador + 1));
                    if (victorias < 1) { throw new IllegalArgumentException(); }
                    for (Puntaje puntaje : puntajes) {
                        if (puntaje.getNombre().equalsIgnoreCase(nombre.trim())) {
                            throw new IllegalArgumentException();
                        }
                    }
                    puntajes.add(new Puntaje(nombre.trim(), victorias));
                } catch (IllegalArgumentException e) {
                    throw new IOException("Hay una línea inválida en el marcador. Se conserva el archivo.", e);
                }
            }
        }
        ordenar(puntajes);
        return puntajes;
    }

    public void registrarVictoria(String nombre) throws IOException {
        validarNombre(nombre);
        nombre = nombre.trim();
        List<Puntaje> puntajes = cargar();
        boolean encontrado = false;
        for (Puntaje puntaje : puntajes) {
            if (puntaje.getNombre().equalsIgnoreCase(nombre)) {
                if (puntaje.getVictorias() == Integer.MAX_VALUE) {
                    throw new IOException("El contador de victorias llegó a su máximo.");
                }
                puntaje.sumarVictoria();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) { puntajes.add(new Puntaje(nombre, 1)); }
        ordenar(puntajes);
        guardar(puntajes);
    }

    private void ordenar(List<Puntaje> puntajes) {
        // Insercion: mas victorias primero; empate por nombre ascendente.
        for (int i = 1; i < puntajes.size(); i++) {
            Puntaje actual = puntajes.get(i);
            int j = i - 1;
            while (j >= 0 && vaAntes(actual, puntajes.get(j))) {
                puntajes.set(j + 1, puntajes.get(j));
                j--;
            }
            puntajes.set(j + 1, actual);
        }
    }

    private boolean vaAntes(Puntaje a, Puntaje b) {
        if (a.getVictorias() != b.getVictorias()) { return a.getVictorias() > b.getVictorias(); }
        return a.getNombre().compareToIgnoreCase(b.getNombre()) < 0;
    }

    private void guardar(List<Puntaje> puntajes) throws IOException {
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta del marcador.");
        }
        // Primero se escribe un temporal. Un error de escritura no borra el record previo.
        File temporal = File.createTempFile("marcador-", ".tmp", carpeta);
        try {
            try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(temporal), "UTF-8"))) {
                escritor.write("nombre;victorias");
                escritor.newLine();
                for (Puntaje puntaje : puntajes) {
                    escritor.write(puntaje.getNombre() + ";" + puntaje.getVictorias());
                    escritor.newLine();
                }
            }
            Files.move(temporal.toPath(), archivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } finally {
            if (temporal.exists()) { temporal.delete(); }
        }
    }

    public String getRuta() { return archivo.getAbsolutePath(); }
}
