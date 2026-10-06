# Quién es Quién — Programación III

Proyecto desarrollado en **Java** para la materia **Programación III**. Es una adaptación del juego *Quién es Quién* con **23 personajes**, interfaz gráfica en **Java Swing** y dos máquinas con estrategias diferentes.

El proyecto no utiliza librerías externas, frameworks ni base de datos. Se ejecuta directamente desde **IntelliJ IDEA** con Java 8 o superior.

## Funcionalidades

- 23 personajes con género, calvicie, lentes y color de pelo.
- Modo **Humano vs Máquina** por etapas.
- Modo **Máquina vs Máquina**.
- Preguntas por características y posibilidad de arriesgar personajes.
- Personajes descartados marcados visualmente en rojo.
- Catálogo agrupado por género, con IDs autoincrementales del 1 al 23 según el alta.
- Récord local de victorias del jugador.
- Interfaz gráfica desarrollada con Swing.

## Estrategias

### Máquina 1 — Estrategia básica

Utiliza un orden fijo de preguntas para reducir sus candidatos.

Además:

- Tiene un **30% de probabilidad de repetir una pregunta anterior**.
- Después de realizar al menos **2 preguntas**, en cada turno tiene un **50% de probabilidad de arriesgar** y un **50% de seguir preguntando**.
- Si arriesga, elige al azar entre los personajes que todavía considera posibles.
- Si falla, ese personaje se descarta y el juego continúa normalmente.

Por ejemplo, con 8 candidatos tiene 1/8 de probabilidad de acertar. Con 2 candidatos tiene 1/2, es decir, **50%**.

### Máquina 2 — Estrategia Greedy

Evalúa las preguntas disponibles y elige la que divide a los candidatos de la forma más equilibrada posible.

```text
diferencia = |cantidadSi - cantidadNo|
```

Selecciona la pregunta con menor diferencia. También tiene un **40% de probabilidad de repetir una pregunta anterior** para evitar que tenga demasiada ventaja.

## Organización del proyecto

```text
QuienEsQuien/
│
├── src/
│   ├── app/
│   │   └── Main.java
│   ├── modelo/
│   │   ├── Personaje.java
│   │   ├── Jugador.java
│   │   ├── Humano.java
│   │   ├── Maquina.java
│   │   ├── Pregunta.java
│   │   ├── Genero.java
│   │   └── ColorPelo.java
│   ├── juego/
│   │   ├── Partida.java
│   │   ├── Tablero.java
│   │   └── CatalogoPersonajes.java
│   ├── estrategia/
│   │   ├── EstrategiaBusqueda.java
│   │   ├── EstrategiaBasica.java
│   │   └── EstrategiaGreedy.java
│   ├── persistencia/
│   │   ├── Marcador.java
│   │   └── Puntaje.java
│   └── ui/
│       ├── VentanaJuego.java
│       └── IconoPersonaje.java
│
├── docs/
│   ├── Documentacion_QuienEsQuien.pdf
│   └── Documentacion_QuienEsQuien.docx
│
├── QuienEsQuien.jar
└── README.md
```

### Packages

| Package | Responsabilidad |
|---|---|
| `app` | Punto de entrada del programa. |
| `modelo` | Personajes, jugadores y datos del juego. |
| `juego` | Reglas, turnos, tablero y coordinación de la partida. |
| `estrategia` | Lógica utilizada por Máquina 1 y Máquina 2. |
| `persistencia` | Guardado y lectura del récord de victorias. |
| `ui` | Interfaz gráfica con Swing. |

## Patrón Strategy

La clase `Maquina` utiliza la interfaz `EstrategiaBusqueda`. Las implementaciones actuales son:

```text
EstrategiaBasica
EstrategiaGreedy
```

De esta forma, cada máquina puede utilizar una estrategia distinta sin mezclar toda la lógica dentro de una misma clase.

## Cómo ejecutar

1. Abrir la carpeta `QuienEsQuien` en IntelliJ IDEA.
2. Configurar un **JDK 8 o superior**.
3. Verificar que la carpeta `src` esté marcada como **Sources Root**.
4. Abrir `src/app/Main.java`.
5. Ejecutar el método `main`.

También puede ejecutarse el JAR desde una terminal con:

```bash
java -jar QuienEsQuien.jar
```

## Récord de victorias

El marcador se guarda localmente en:

```text
~/QuienEsQuien/marcador.csv
```

En Windows, `~` corresponde a la carpeta del usuario.

## Tecnologías utilizadas

- Java
- Java Swing
- Programación Orientada a Objetos
- Herencia e interfaces
- Enumeraciones y colecciones
- Patrón Strategy
- Estrategia Greedy
- Persistencia en archivos CSV

## Materia

**Programación III**  
Trabajo práctico — *Quién es Quién*.
