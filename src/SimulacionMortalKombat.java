import java.util.InputMismatchException;
import java.util.Scanner;

class Personaje {
    String nombre;
    int fuerza;
    int velocidad;
    int resistencia;

    public Personaje(String nombre, int fuerza, int velocidad, int resistencia) {
        this.nombre = nombre;
        this.fuerza = fuerza;
        this.velocidad = velocidad;
        this.resistencia = resistencia;
    }

    public void atacar(Personaje oponente) {
        System.out.println(nombre + " ataca a " + oponente.nombre + " con fuerza de " + fuerza + " puntos.");
        oponente.resistencia -= fuerza;
        if (oponente.resistencia < 0)
            oponente.resistencia = 0;
        System.out.println(oponente.nombre + " ahora tiene " + oponente.resistencia + " puntos de resistencia.\n");
    }

    public void mostrarEstadisticas() {
        System.out.println("--- Estadísticas de " + nombre + " ---");
        System.out.println("Fuerza: " + fuerza);
        System.out.println("Velocidad: " + velocidad);
        System.out.println("Resistencia: " + resistencia);
        System.out.println("-----------------------------------\n");
    }

    public void recuperarse() {
        resistencia += 15;
        System.out.println(nombre + " se recupera y ahora tiene " + resistencia + " puntos de resistencia.\n");
    }

    public int ataqueEspecial(Personaje oponente) {
        int danio = fuerza + velocidad;
        int resistenciaInicial = oponente.resistencia;
        oponente.resistencia -= danio;
        if (oponente.resistencia < 0)
            oponente.resistencia = 0;
        int danioReal = resistenciaInicial - oponente.resistencia;
        System.out.println(nombre + " realiza un ATAQUE ESPECIAL a " + oponente.nombre + " causando " + danioReal
                + " puntos de daño.");
        System.out.println(oponente.nombre + " ahora tiene " + oponente.resistencia + " puntos de resistencia.\n");
        return danioReal;
    }
}

public class SimulacionMortalKombat {

    public static void fraseFinal(String ganador, String perdedor) {
        System.out.println("" + perdedor + " está en las últimas");
        System.out.println("" + ganador.toUpperCase() + " \"FINISH HIM!\"");
        System.out.println("" + ganador + " realiza un FATALITY sobre " + perdedor + "");
        System.out.println("¡" + ganador + " gana la batalla!");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Personaje[] personajes = {
                new Personaje("Scorpion", 30, 20, 100),
                new Personaje("Sub-Zero", 28, 22, 110),
                new Personaje("Liu Kang", 32, 25, 95),
                new Personaje("Raiden", 27, 23, 105),
                new Personaje("Kitana", 25, 28, 90),
                new Personaje("Kung Lao", 29, 24, 97),
                new Personaje("Sonya Blade", 26, 26, 92),
                new Personaje("Jax", 33, 18, 120),
                new Personaje("Mileena", 28, 27, 89),
                new Personaje("Reptile", 27, 25, 93)
        };

        System.out.println("=== Selección de Personajes Mortal Kombat ===");
        for (int i = 0; i < personajes.length; i++) {
            System.out.println((i + 1) + ". " + personajes[i].nombre);
        }

        int eleccion1 = 0;
        while (true) {
            System.out.print("\nJugador 1, elige tu personaje (1-10): ");
            try {
                eleccion1 = scanner.nextInt();
                if (eleccion1 < 1 || eleccion1 > 10) {
                    System.out.println("Opción no válida. Elige un número del 1 al 10.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Opción no válida. Debes ingresar un número.");
                scanner.next();
            }
        }
        Personaje jugador1 = personajes[eleccion1 - 1];
        System.out.println("Jugador 1 ha elegido: " + jugador1.nombre);

        int eleccion2 = 0;
        while (true) {
            System.out.print("Jugador 2, elige tu personaje (1-10): ");
            try {
                eleccion2 = scanner.nextInt();
                if (eleccion2 < 1 || eleccion2 > 10) {
                    System.out.println("Opción no válida. Elige un número del 1 al 10.");
                } else if (eleccion2 == eleccion1) {
                    System.out.println("Ese personaje ya fue elegido. Elige otro.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Opción no válida. Debes ingresar un número.");
                scanner.next();
            }
        }
        Personaje jugador2 = personajes[eleccion2 - 1];
        System.out.println("Jugador 2 ha elegido: " + jugador2.nombre);

        System.out.println("\nEstadísticas iniciales:");
        jugador1.mostrarEstadisticas();
        jugador2.mostrarEstadisticas();

        int turno = 1;
        boolean batallaActiva = true;
        while (batallaActiva) {
            Personaje atacante = (turno == 1) ? jugador1 : jugador2;
            Personaje defensor = (turno == 1) ? jugador2 : jugador1;

            int opcion = 0;
            boolean opcionValida = false;
            while (!opcionValida) {
                System.out.println("Turno de " + atacante.nombre);
                System.out.println("1. Atacar");
                System.out.println("2. Ataque especial");
                System.out.println("3. Recuperarse");
                System.out.println("4. Mostrar estadísticas");
                System.out.println("5. Terminar batalla");
                System.out.print("Elige una opción: ");
                try {
                    opcion = scanner.nextInt();
                    if (opcion < 1 || opcion > 5) {
                        System.out.println("Opción no válida. Debes elegir entre 1 y 5.");
                    } else {
                        opcionValida = true;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Opción no válida. Debes ingresar un número.");
                    scanner.next();
                }
            }

            switch (opcion) {
                case 1:
                    atacante.atacar(defensor);
                    if (defensor.resistencia == 0) {
                        fraseFinal(atacante.nombre, defensor.nombre);
                        batallaActiva = false;
                    }
                    break;
                case 2:
                    try {
                        atacante.ataqueEspecial(defensor);
                        if (defensor.resistencia == 0) {
                            fraseFinal(atacante.nombre, defensor.nombre);
                            batallaActiva = false;
                        }
                    } catch (Exception e) {
                        System.out.println("Ocurrió un error al ejecutar el ataque especial: " + e.getMessage());
                    }
                    break;
                case 3:
                    atacante.recuperarse();
                    break;
                case 4:
                    atacante.mostrarEstadisticas();
                    defensor.mostrarEstadisticas();
                    break;
                case 5:
                    System.out.println("La batalla ha terminado por decisión de los jugadores.");
                    batallaActiva = false;
                    break;
            }

            if (batallaActiva) {
                turno = (turno == 1) ? 2 : 1;
            }
        }

        scanner.close();
    }
}