import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

abstract class Personaje {
    private String nombre;
    private int fuerza;
    private int velocidad;
    private int resistencia;
    private int resistenciaMaxima;
    private String universo;
    private boolean poderEspecialUsado;
    protected static Random random = new Random();

    public Personaje(String nombre, int fuerza, int velocidad, int resistencia, String universo) {
        this.nombre = nombre;
        this.fuerza = fuerza;
        this.velocidad = velocidad;
        this.resistencia = resistencia;
        this.resistenciaMaxima = resistencia;
        this.universo = universo;
        this.poderEspecialUsado = false;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getFuerza() { return fuerza; }
    public void setFuerza(int fuerza) { this.fuerza = Math.max(0, fuerza); }

    public int getVelocidad() { return velocidad; }
    public void setVelocidad(int velocidad) { this.velocidad = Math.max(0, velocidad); }

    public int getResistencia() { return resistencia; }
    public void setResistencia(int resistencia) {
        this.resistencia = Math.max(0, Math.min(resistencia, resistenciaMaxima));
    }

    public int getResistenciaMaxima() { return resistenciaMaxima; }
    public String getUniverso() { return universo; }
    public boolean isPoderEspecialUsado() { return poderEspecialUsado; }
    public void setPoderEspecialUsado(boolean usado) { this.poderEspecialUsado = usado; }

    public void atacar(Personaje oponente) {
        System.out.println(nombre + " ataca a " + oponente.nombre + " con fuerza de " + fuerza + " puntos.");
        oponente.setResistencia(oponente.getResistencia() - fuerza);
        System.out.println(oponente.nombre + " ahora tiene " + oponente.getResistencia() + " puntos de resistencia.\n");
    }

    public void atacar(Personaje oponente, int bonusFuerza) {
        int danioTotal = fuerza + bonusFuerza;
        System.out.println(nombre + " ataca con un bonus de fuerza, causando " + danioTotal + " puntos de daño a " + oponente.nombre);
        oponente.setResistencia(oponente.getResistencia() - danioTotal);
        System.out.println(oponente.nombre + " ahora tiene " + oponente.getResistencia() + " puntos de resistencia.\n");
    }

    public void mostrarEstadisticas() {
        System.out.println("--- Estadísticas de " + nombre + " (" + universo + ") ---");
        System.out.println("Fuerza: " + fuerza);
        System.out.println("Velocidad: " + velocidad);
        System.out.println("Resistencia: " + resistencia + "/" + resistenciaMaxima);
        System.out.println("Poder especial usado: " + (poderEspecialUsado ? "Sí" : "No"));
        System.out.println("-----------------------------------\n");
    }

    public int recuperarse() {
        int resistenciaInicial = this.resistencia;
        int maxRecuperacion = this.resistenciaMaxima - resistenciaInicial;
        
        if (maxRecuperacion <= 0) {
            System.out.println(" "+ nombre + " ya tiene la resistencia al máximo (" + resistenciaMaxima + "). ¡No puede recuperarse más!");
            return 0;
        }
        
        int recuperacionBase = 15;
        int bonus = random.nextInt(11);
        int recuperacionTotal = Math.min(recuperacionBase + bonus, maxRecuperacion);
        
        this.resistencia += recuperacionTotal;

        if (recuperacionTotal < (recuperacionBase + bonus)) {
            System.out.println(" " + nombre + " se recupera en " + recuperacionTotal  + " puntos (máximo posible) y ahora tiene " + resistencia + "/" + resistenciaMaxima + " de resistencia.\n");
        } else {
            System.out.println(" " + nombre + " se recupera en " + recuperacionTotal  + " puntos y ahora tiene " + resistencia + "/"  + resistenciaMaxima + " de resistencia.\n");
        }
        
        return recuperacionTotal;
    }

    public int ataqueEspecial(Personaje oponente) {
        int danio = fuerza + velocidad;
        int resistenciaInicial = oponente.getResistencia();
        oponente.setResistencia(oponente.getResistencia() - danio);
        int danioReal = resistenciaInicial - oponente.getResistencia();
        System.out.println(nombre + " realiza un ATAQUE ESPECIAL a " + oponente.nombre + " causando " + danioReal + " puntos de daño.");
        System.out.println(oponente.nombre + " ahora tiene " + oponente.getResistencia() + " puntos de resistencia.\n");
        return danioReal;
    }

    public abstract int poderUnico(Personaje oponente);
    public abstract String getDescripcionPoder();
}

class PersonajeMK extends Personaje {
    private String habilidadEspecial;

    public PersonajeMK(String nombre, int fuerza, int velocidad, int resistencia, String habilidadEspecial) {
        super(nombre, fuerza, velocidad, resistencia, "Mortal Kombat");
        this.habilidadEspecial = habilidadEspecial;
    }

    public String getHabilidadEspecial() { return habilidadEspecial; }

    public int poderUnico(Personaje oponente) {
        if (isPoderEspecialUsado()) {
            System.out.println(getNombre() + " ya usó su poder único en esta batalla!");
            return 0;
        }

        setPoderEspecialUsado(true);
        int danio = 0;

        switch (getNombre()) {
            case "Scorpion":
                danio = poderInfernal(oponente);
                break;
            case "Sub-Zero":
                danio = poderHelado(oponente);
                break;
            case "Liu Kang":
                danio = poderDragon(oponente);
                break;
            case "Raiden":
                danio = poderRayo(oponente);
                break;
            case "Kitana":
                danio = poderAbanicos(oponente);
                break;
            default:
                danio = getFuerza() * 2;
                System.out.println(getNombre() + " usa su poder único causando " + danio + " puntos de daño!");
        }

        return danio;
    }

    private int poderInfernal(Personaje oponente) {
        int danio = getFuerza() * 3;
        System.out.println(" " + getNombre() + " grita '¡GET OVER HERE!' y lanza su kunai flamígero!");
        System.out.println("¡El oponente es arrastrado hacia las llamas del infierno!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    private int poderHelado(Personaje oponente) {
        int danio = getFuerza() * 2;
        System.out.println(" " + getNombre() + " congela completamente a " + oponente.getNombre() + "!");
        System.out.println("¡El hielo causa daño y ralentiza al oponente!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        oponente.setVelocidad(oponente.getVelocidad() - 5);
        return danio;
    }

    private int poderDragon(Personaje oponente) {
        int danio = (getFuerza() + getVelocidad()) * 2;
        System.out.println(" " + getNombre() + " se transforma en dragón de fuego!");
        System.out.println("¡Un dragón ardiente impacta contra el oponente!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    private int poderRayo(Personaje oponente) {
        int danio = getFuerza() * 4;
        System.out.println(" " + getNombre() + " invoca el poder del trueno!");
        System.out.println("¡Un rayo divino golpea desde los cielos!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    private int poderAbanicos(Personaje oponente) {
        int danio = getVelocidad() * 3;
        System.out.println(" " + getNombre() + " crea un tornado mortal con sus abanicos!");
        System.out.println("¡Las cuchillas giratorias causan múltiples cortes!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    public String getDescripcionPoder() {
        switch (getNombre()) {
            case "Scorpion": return "Cadenas Infernales - Daño masivo de fuego";
            case "Sub-Zero": return "Congelamiento Total - Daño y ralentización";
            case "Liu Kang": return "Transformación Dragón - Ataque devastador";
            case "Raiden": return "Rayo Divino - Poder del trueno";
            case "Kitana": return "Tornado de Abanicos - Cortes múltiples";
            default: return "Poder único desconocido";
        }
    }
}

class PersonajeDC extends Personaje {
    private String superpoder;

    public PersonajeDC(String nombre, int fuerza, int velocidad, int resistencia, String superpoder) {
        super(nombre, fuerza, velocidad, resistencia, "DC Comics");
        this.superpoder = superpoder;
    }

    public String getSuperpoder() { return superpoder; }

    public int poderUnico(Personaje oponente) {
        if (isPoderEspecialUsado()) {
            System.out.println(getNombre() + " ya usó su poder único en esta batalla!");
            return 0;
        }

        setPoderEspecialUsado(true);
        int danio = 0;

        switch (getNombre()) {
            case "Superman":
                danio = poderKryptoniano(oponente);
                break;
            case "Batman":
                danio = poderTecnologico(oponente);
                break;
            case "Wonder Woman":
                danio = poderAmazonica(oponente);
                break;
            case "Flash":
                danio = poderVelocidad(oponente);
                break;
            case "Green Lantern":
                danio = poderAnillo(oponente);
                break;
            default:
                danio = getFuerza() * 2;
                System.out.println(getNombre() + " usa su superpoder causando " + danio + " puntos de daño!");
        }

        return danio;
    }

    private int poderKryptoniano(Personaje oponente) {
        int danio = getFuerza() * 3;
        System.out.println(" " + getNombre() + " usa su visión de calor y superfuerza!");
        System.out.println("¡Los rayos láser y el puño de acero impactan devastadoramente!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    private int poderTecnologico(Personaje oponente) {
        int danio = getFuerza() * 3;
        System.out.println(" " + getNombre() + " despliega todo su arsenal tecnológico!");
        System.out.println("¡Batarangs explosivos y gadgets causan daño múltiple!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        setResistencia(getResistencia() + 10);
        System.out.println(getNombre() + " recupera algo de resistencia gracias a su preparación.");
        return danio;
    }

    private int poderAmazonica(Personaje oponente) {
        int danio = (getFuerza() + getVelocidad()) * 2;
        System.out.println("⚔️ " + getNombre() + " invoca el poder de los dioses griegos!");
        System.out.println("¡Su lazo de la verdad y espada divina atacan!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    private int poderVelocidad(Personaje oponente) {
        int danio = getVelocidad() * 4;
        System.out.println(" " + getNombre() + " corre a la velocidad de la luz!");
        System.out.println("¡Múltiples golpes en una fracción de segundo!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    private int poderAnillo(Personaje oponente) {
        int danio = getFuerza() * 3;
        System.out.println(" " + getNombre() + " crea constructos de energía verde!");
        System.out.println("¡Un martillo gigante de luz verde aplasta al oponente!");
        oponente.setResistencia(oponente.getResistencia() - danio);
        return danio;
    }

    public String getDescripcionPoder() {
        switch (getNombre()) {
            case "Superman": return "Poder Kryptoniano - Visión láser y superfuerza";
            case "Batman": return "Arsenal Tecnológico - Gadgets y estrategia";
            case "Wonder Woman": return "Poder Amazónico - Fuerza divina griega";
            case "Flash": return "Fuerza de Velocidad - Ataques súper rápidos";
            case "Green Lantern": return "Anillo de Poder - Constructos de energía";
            default: return "Superpoder desconocido";
        }
    }
}

class SistemaTorneo {
    private String[] historialGanadores;
    private int[] puntajesJugadores;
    private int partidasJugadas;
    private static final int MAX_PARTIDAS = 10;

    public SistemaTorneo() {
        this.historialGanadores = new String[MAX_PARTIDAS];
        this.puntajesJugadores = new int[2];
        this.partidasJugadas = 0;
    }

    public void registrarVictoria(String ganador, int numeroJugador, int puntaje) {
        if (partidasJugadas < MAX_PARTIDAS) {
            historialGanadores[partidasJugadas] = ganador;
            puntajesJugadores[numeroJugador - 1] += puntaje;
            partidasJugadas++;
        }
    }

    public void mostrarRanking() {
        System.out.println(" === RANKING DEL TORNEO === ");
        System.out.println("Partidas jugadas: " + partidasJugadas + "/" + MAX_PARTIDAS);
        System.out.println("Puntaje Jugador 1: " + puntajesJugadores[0]);
        System.out.println("Puntaje Jugador 2: " + puntajesJugadores[1]);

        if (partidasJugadas > 0) {
            System.out.println("\nHistorial de ganadores:");
            for (int i = 0; i < partidasJugadas; i++) {
                System.out.println("Partida " + (i + 1) + ": " + historialGanadores[i]);
            }
        }

        String lider = "Empate";
        if (puntajesJugadores[0] > puntajesJugadores[1]) {
            lider = "Jugador 1";
        } else if (puntajesJugadores[1] > puntajesJugadores[0]) {
            lider = "Jugador 2";
        }
        System.out.println(" Líder actual: " + lider);
        System.out.println("============================\n");
    }

    public boolean puedeJugarMas() {
        return partidasJugadas < MAX_PARTIDAS;
    }

    public int getPartidasJugadas() { return partidasJugadas; }
    public int getPuntajeJugador(int numeroJugador) {
        return puntajesJugadores[numeroJugador - 1];
    }
}

class EstadisticasBatalla {
    private int ataques;
    private int ataquesCriticos;
    private int poderesUsados;
    private int recuperaciones;
    private int totalPuntosRecuperados;

    public EstadisticasBatalla() {
        this.ataques = 0;
        this.ataquesCriticos = 0;
        this.poderesUsados = 0;
        this.recuperaciones = 0;
        this.totalPuntosRecuperados = 0;
    }

    public void registrarAtaque() { ataques++; }
    public void registrarAtaqueCritico() { ataquesCriticos++; }
    public void registrarPoderUsado() { poderesUsados++; }
    public void registrarRecuperacion(int puntos) {
        recuperaciones++;
        totalPuntosRecuperados += puntos;
    }

    public void mostrarEstadisticas() {
        System.out.println(" === ESTADÍSTICAS DE LA ÚLTIMA BATALLA === ");
        System.out.println("Ataques totales: " + ataques);
        System.out.println("Ataques críticos: " + ataquesCriticos);
        System.out.println("Poderes únicos usados: " + poderesUsados);
        System.out.println("Recuperaciones: " + recuperaciones + " (Total recuperado: " + totalPuntosRecuperados + " puntos)");
        System.out.println("===========================================\n");
    }
}

public class MortalKombat {
    private static Random random = new Random();
    private static SistemaTorneo torneo = new SistemaTorneo();

    public static void mostrarTitulo() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║             MORTAL KOMBAT VS DC COMICS                   ║");
        System.out.println("║                   BATALLA ÉPICA                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    public static void fraseFinal(String ganador, String perdedor) {
        System.out.println("\n FINAL ÉPICO ");
        System.out.println(perdedor + " está en las últimas...");
        System.out.println(ganador.toUpperCase() + ": \"FINISH HIM/HER!\" ");
        System.out.println(" " + ganador + " realiza un FATALITY sobre " + perdedor + " ");
        System.out.println(" ¡" + ganador + " GANA LA BATALLA INTERDIMENSIONAL! ");
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    public static Personaje[] crearPersonajes() {
        return new Personaje[] {
            new PersonajeMK("Scorpion", 35, 25, 120, "Cadenas del Infierno"),
            new PersonajeMK("Sub-Zero", 30, 28, 125, "Maestro del Hielo"),
            new PersonajeMK("Liu Kang", 38, 30, 110, "Dragón de Fuego"),
            new PersonajeMK("Raiden", 32, 27, 130, "Dios del Trueno"),
            new PersonajeMK("Kitana", 28, 35, 105, "Princesa Asesina"),

            new PersonajeDC("Superman", 45, 30, 150, "Poder Kryptoniano"),
            new PersonajeDC("Batman", 25, 32, 100, "Genio Táctico"),
            new PersonajeDC("Wonder Woman", 40, 28, 135, "Guerrera Amazónica"),
            new PersonajeDC("Flash", 20, 50, 90, "Fuerza de Velocidad"),
            new PersonajeDC("Green Lantern", 35, 25, 115, "Anillo de Poder")
        };
    }

    public static void mostrarPersonajes(Personaje[] personajes) {
        System.out.println("═══ SELECCIÓN DE LUCHADORES ═══");
        System.out.println("MORTAL KOMBAT:");
        for (int i = 0; i < personajes.length; i++) {
            if (i == 0) System.out.println("--- MORTAL KOMBAT ---");
            if (i == 5) System.out.println("\n--- DC COMICS ---");
            System.out.printf("%d. %s (%s) - Poder: %s\n", (i + 1), personajes[i].getNombre(),
                              personajes[i].getUniverso(), personajes[i].getDescripcionPoder());
        }
        System.out.println("═══════════════════════════════");
    }

    public static int seleccionarPersonaje(Scanner scanner, String jugador, Personaje[] catalogoPersonajes, int[] personajesYaElegidos) {
        int eleccion = 0;
        boolean valido = false;
        int numOpciones = catalogoPersonajes.length;

        while (!valido) {
            try {
                System.out.print(jugador + ", elige tu luchador (1-" + numOpciones + "): ");
                eleccion = scanner.nextInt();

                if (eleccion < 1 || eleccion > numOpciones) {
                    throw new IllegalArgumentException("Número fuera de rango. Debes elegir entre 1 y " + numOpciones + ".");
                }

                boolean yaUsado = false;
                for (int usado : personajesYaElegidos) {
                    if (usado == eleccion) {
                        yaUsado = true;
                        break;
                    }
                }

                if (yaUsado) {
                    System.out.println(" Ese luchador ya fue elegido. Elige otro.");
                } else {
                    valido = true;
                }

            } catch (InputMismatchException e) {
                System.out.println(" Error: Debes ingresar un número válido.");
                scanner.next();
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println(" Error inesperado: " + e.getMessage());
                if (scanner.hasNext()) scanner.next();
            }
        }
        return eleccion;
    }

    public static boolean ataqueConCritico(Personaje atacante, Personaje defensor) {
        boolean critico = random.nextInt(100) < 15;

        if (critico) {
            System.out.println("¡GOLPE CRÍTICO! ");
            atacante.atacar(defensor, atacante.getFuerza());
            return true;
        } else {
            atacante.atacar(defensor);
            return false;
        }
    }

    public static int ejecutarTurno(Scanner scanner, Personaje atacante, Personaje defensor, EstadisticasBatalla stats) {
        int opcion = 0;
        boolean opcionValida = false;
        String[] opcionesAccion = {
            "1. Atacar",
            "2. Ataque Especial",
            "3. Poder Único (" + atacante.getDescripcionPoder() + (atacante.isPoderEspecialUsado() ? " - Ya usado" : " - Disponible") + ")",
            "4. Recuperarse",
            "5. Ver Estadísticas de ambos",
            "6. Huir de la batalla (Derrota)"
        };

        while (!opcionValida) {
            System.out.println("\n🎮 Turno de " + atacante.getNombre() + " (" + atacante.getUniverso() + ")");
            System.out.println("Resistencia: " + atacante.getResistencia() + "/" + atacante.getResistenciaMaxima() + " | Oponente: " + defensor.getNombre() + " " + defensor.getResistencia() + "/" + defensor.getResistenciaMaxima());

            for (String textoOpcion : opcionesAccion) {
                System.out.println(textoOpcion);
            }

            try {
                System.out.print("Elige tu acción (1-" + opcionesAccion.length + "): ");
                opcion = scanner.nextInt();

                if (opcion < 1 || opcion > opcionesAccion.length) {
                    throw new IllegalArgumentException("Opción no válida");
                }
                opcionValida = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: Debes ingresar un número.");
                scanner.next();
            } catch (IllegalArgumentException e) {
                System.out.println(" " + e.getMessage() + ". Elige entre 1 y " + opcionesAccion.length + ".");
            } catch (Exception e) {
                System.out.println(" Error inesperado: " + e.getMessage());
                if (scanner.hasNext()) scanner.next();
            }
        }

        switch (opcion) {
            case 1:
                if (ataqueConCritico(atacante, defensor)) {
                    stats.registrarAtaqueCritico();
                } else {
                    stats.registrarAtaque();
                }
                return 0;
            case 2:
                atacante.ataqueEspecial(defensor);
                stats.registrarAtaque();
                return 0;
            case 3:
                if (atacante.isPoderEspecialUsado()) {
                    System.out.println(atacante.getNombre() + " ya ha usado su Poder Único. Elige otra acción.");
                    return ejecutarTurno(scanner, atacante, defensor, stats);
                }
                int danio = atacante.poderUnico(defensor);
                if (danio > 0) {
                    stats.registrarPoderUsado();
                }
                return 0;
            case 4:
                int puntosRecuperados = atacante.recuperarse();
                stats.registrarRecuperacion(puntosRecuperados);
                return 0;
            case 5:
                atacante.mostrarEstadisticas();
                defensor.mostrarEstadisticas();
                return ejecutarTurno(scanner, atacante, defensor, stats);
            case 6:
                System.out.println(" " + atacante.getNombre() + " decide huir de la batalla!");
                System.out.println(" " + defensor.getNombre() + " gana por abandono!");
                return 1;
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        mostrarTitulo();

        boolean continuarJugando = true;
        while (continuarJugando && torneo.puedeJugarMas()) {
            EstadisticasBatalla stats = new EstadisticasBatalla();
            Personaje[] personajes = crearPersonajes();
            mostrarPersonajes(personajes);

            int[] personajesUsados = new int[2];
            System.out.println("\n--- Selección Jugador 1 ---");
            personajesUsados[0] = seleccionarPersonaje(scanner, " Jugador 1", personajes, new int[]{});
            Personaje jugador1 = personajes[personajesUsados[0] - 1];
            System.out.println(" Jugador 1 eligió: " + jugador1.getNombre() + "\n");

            System.out.println("--- Selección Jugador 2 ---");
            personajesUsados[1] = seleccionarPersonaje(scanner, " Jugador 2", personajes, new int[]{personajesUsados[0]});
            Personaje jugador2 = personajes[personajesUsados[1] - 1];
            System.out.println(" Jugador 2 eligió: " + jugador2.getNombre() + "\n");

            jugador1.setPoderEspecialUsado(false);
            jugador2.setPoderEspecialUsado(false);

            System.out.println("\n ¡COMIENZA LA BATALLA INTERDIMENSIONAL! ");
            jugador1.mostrarEstadisticas();
            jugador2.mostrarEstadisticas();

            int turno = random.nextInt(2) + 1;
            System.out.println(" Por sorteo, ¡empieza el Jugador " + turno + "!\n");

            int rondas = 0;
            Personaje ganador = null;
            int numeroJugadorGanador = 0;
            boolean batallaActiva = true;
            boolean victoriaPorHuir = false;

            while (batallaActiva) {
                rondas++;
                System.out.println(" RONDA " + rondas + " ");

                Personaje atacante = (turno == 1) ? jugador1 : jugador2;
                Personaje defensor = (turno == 1) ? jugador2 : jugador1;

                int resultadoTurno = ejecutarTurno(scanner, atacante, defensor, stats);
                
                if (resultadoTurno == 1) {
                    ganador = defensor;
                    numeroJugadorGanador = (defensor == jugador1) ? 1 : 2;
                    victoriaPorHuir = true;
                    batallaActiva = false;
                } else if (defensor.getResistencia() <= 0) {
                    fraseFinal(atacante.getNombre(), defensor.getNombre());
                    ganador = atacante;
                    numeroJugadorGanador = turno;
                    batallaActiva = false;
                } else {
                    turno = (turno == 1) ? 2 : 1;
                    if (rondas % 3 == 0) {
                        System.out.println("\n Estado después de " + rondas + " rondas:");
                        System.out.println(jugador1.getNombre() + ": " + jugador1.getResistencia() + " HP");
                        System.out.println(jugador2.getNombre() + ": " + jugador2.getResistencia() + " HP\n");
                    }
                }
            }

            if (ganador != null && !victoriaPorHuir) {
                int puntaje = ganador.getResistencia() + (rondas <= 10 ? 100 : 50);
                torneo.registrarVictoria(ganador.getNombre(), numeroJugadorGanador, puntaje);
            } else if (victoriaPorHuir) {
                torneo.registrarVictoria(ganador.getNombre(), numeroJugadorGanador, 50);
            }

            stats.mostrarEstadisticas();
            torneo.mostrarRanking();

            if (torneo.puedeJugarMas()) {
                boolean respuestaValida = false;
                while (!respuestaValida) {
                    try {
                        System.out.print("¿Desean jugar otra partida? (1=Sí, 2=No): ");
                        int respuesta = scanner.nextInt();
                        if (respuesta == 1) {
                            continuarJugando = true;
                            respuestaValida = true;
                        } else if (respuesta == 2) {
                            continuarJugando = false;
                            respuestaValida = true;
                        } else {
                            System.out.println(" Respuesta inválida. Ingresa 1 o 2.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println(" Error: Debes ingresar un número.");
                        scanner.next();
                    }
                }
            } else {
                System.out.println(" ¡Se ha alcanzado el máximo de partidas del torneo!");
                continuarJugando = false;
            }
        }

        System.out.println("\n ¡Gracias por jugar Mortal Kombat vs DC Comics! ");
        System.out.println(" RESULTADO FINAL DEL TORNEO ");
        torneo.mostrarRanking();
        scanner.close();
    }
}