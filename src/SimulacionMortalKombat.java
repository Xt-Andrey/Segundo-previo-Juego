import java.util.*;

// Enums para mejor organización y type safety
enum TipoAccion {
    ATACAR(1, "👊 Atacar"),
    ATAQUE_ESPECIAL(2, "⚡ Ataque Especial"),
    PODER_UNICO(3, "🌟 Poder Único"),
    RECUPERARSE(4, "💚 Recuperarse"),
    VER_ESTADISTICAS(5, "📊 Ver Estadísticas"),
    HUIR(6, "🏃 Huir de la batalla");

    private final int codigo;
    private final String descripcion;

    TipoAccion(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }

    public static TipoAccion fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(accion -> accion.codigo == codigo)
                .findFirst()
                .orElse(null);
    }
}

enum ResultadoBatalla {
    CONTINUA, VICTORIA, DERROTA, HUIDA
}

// Clase base abstracta optimizada
abstract class Personaje {
    protected final String nombre;
    protected final String universo;
    protected int fuerza;
    protected int velocidad;
    protected int resistencia;
    protected final int resistenciaMaxima;
    protected boolean poderEspecialUsado;

    protected Personaje(String nombre, int fuerza, int velocidad, int resistencia, String universo) {
        this.nombre = nombre;
        this.fuerza = fuerza;
        this.velocidad = velocidad;
        this.resistencia = resistencia;
        this.resistenciaMaxima = resistencia;
        this.universo = universo;
        this.poderEspecialUsado = false;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getUniverso() { return universo; }
    public int getFuerza() { return fuerza; }
    public int getVelocidad() { return velocidad; }
    public int getResistencia() { return resistencia; }
    public int getResistenciaMaxima() { return resistenciaMaxima; }
    public boolean isPoderEspecialUsado() { return poderEspecialUsado; }

    // Setters optimizados
    public void setFuerza(int fuerza) { 
        this.fuerza = Math.max(0, fuerza); 
    }
    
    public void setVelocidad(int velocidad) { 
        this.velocidad = Math.max(0, velocidad); 
    }
    
    public void setResistencia(int resistencia) {
        this.resistencia = Math.max(0, Math.min(resistencia, resistenciaMaxima));
    }
    
    public void setPoderEspecialUsado(boolean usado) { 
        this.poderEspecialUsado = usado; 
    }

    public void resetearEstado() {
        this.resistencia = resistenciaMaxima;
        this.poderEspecialUsado = false;
    }

    public boolean estaVivo() { 
        return resistencia > 0; 
    }

    // Métodos de combate optimizados
    public int atacar(Personaje oponente) {
        System.out.printf("%s ataca a %s con %d puntos de fuerza.%n", 
                         nombre, oponente.nombre, fuerza);
        int danioRealizado = Math.min(fuerza, oponente.resistencia);
        oponente.setResistencia(oponente.resistencia - fuerza);
        System.out.printf("%s ahora tiene %d puntos de resistencia.%n%n", 
                         oponente.nombre, oponente.resistencia);
        return danioRealizado;
    }

    public int atacarConBonus(Personaje oponente, int bonusFuerza) {
        int danioTotal = fuerza + bonusFuerza;
        System.out.printf("%s ataca con bonus, causando %d puntos de daño a %s%n", 
                         nombre, danioTotal, oponente.nombre);
        int danioRealizado = Math.min(danioTotal, oponente.resistencia);
        oponente.setResistencia(oponente.resistencia - danioTotal);
        System.out.printf("%s ahora tiene %d puntos de resistencia.%n%n", 
                         oponente.nombre, oponente.resistencia);
        return danioRealizado;
    }

    public void mostrarEstadisticas() {
        System.out.printf("--- %s (%s) ---%n", nombre, universo);
        System.out.printf("Fuerza: %d | Velocidad: %d%n", fuerza, velocidad);
        System.out.printf("Resistencia: %d/%d%n", resistencia, resistenciaMaxima);
        System.out.printf("Poder especial: %s%n", poderEspecialUsado ? "Usado" : "Disponible");
        System.out.println("-------------------------\n");
    }

    public void recuperarse() {
        recuperarse(15);
    }

    public void recuperarse(int puntosRecuperados) {
        int resistenciaAnterior = resistencia;
        setResistencia(resistencia + puntosRecuperados);
        int recuperacionReal = resistencia - resistenciaAnterior;
        System.out.printf("%s se recupera %d puntos (Resistencia: %d)%n%n", 
                         nombre, recuperacionReal, resistencia);
    }

    public int ataqueEspecial(Personaje oponente) {
        int danio = fuerza + velocidad;
        System.out.printf("%s realiza ATAQUE ESPECIAL causando %d puntos de daño%n", 
                         nombre, danio);
        int danioRealizado = Math.min(danio, oponente.resistencia);
        oponente.setResistencia(oponente.resistencia - danio);
        System.out.printf("%s ahora tiene %d puntos de resistencia.%n%n", 
                         oponente.nombre, oponente.resistencia);
        return danioRealizado;
    }

    // Métodos abstractos
    public abstract int poderUnico(Personaje oponente);
    public abstract String getDescripcionPoder();
}

// Implementación optimizada para Mortal Kombat
class PersonajeMK extends Personaje {
    private static final Map<String, PoderMK> PODERES_MK = Map.of(
        "Scorpion", new PoderMK("Cadenas Infernales", "🔥 ¡GET OVER HERE!", 3),
        "Sub-Zero", new PoderMK("Congelamiento Total", "❄️ Hielo mortal", 2),
        "Liu Kang", new PoderMK("Transformación Dragón", "🐉 Dragón de fuego", 2),
        "Raiden", new PoderMK("Rayo Divino", "⚡ Poder del trueno", 4),
        "Kitana", new PoderMK("Tornado de Abanicos", "🌪️ Cuchillas giratorias", 3)
    );

    private final String habilidadEspecial;

    public PersonajeMK(String nombre, int fuerza, int velocidad, int resistencia, String habilidadEspecial) {
        super(nombre, fuerza, velocidad, resistencia, "Mortal Kombat");
        this.habilidadEspecial = habilidadEspecial;
    }

    @Override
    public int poderUnico(Personaje oponente) {
        if (poderEspecialUsado) {
            System.out.println(nombre + " ya usó su poder único!");
            return 0;
        }

        poderEspecialUsado = true;
        PoderMK poder = PODERES_MK.getOrDefault(nombre, 
            new PoderMK("Poder Desconocido", "Ataque especial", 2));
        
        int danio = fuerza * poder.multiplicador;
        System.out.println(poder.mensaje);
        System.out.printf("¡%d puntos de daño!%n", danio);
        
        oponente.setResistencia(oponente.resistencia - danio);
        
        // Efectos especiales
        if ("Sub-Zero".equals(nombre)) {
            oponente.setVelocidad(oponente.velocidad - 5);
            System.out.println("¡El oponente se ralentiza!");
        }
        
        return Math.min(danio, oponente.resistencia + danio);
    }

    @Override
    public String getDescripcionPoder() {
        return PODERES_MK.getOrDefault(nombre, 
            new PoderMK("Poder Desconocido", "", 1)).nombre;
    }

    private static class PoderMK {
        final String nombre;
        final String mensaje;
        final int multiplicador;

        PoderMK(String nombre, String mensaje, int multiplicador) {
            this.nombre = nombre;
            this.mensaje = mensaje;
            this.multiplicador = multiplicador;
        }
    }
}

// Implementación optimizada para DC Comics
class PersonajeDC extends Personaje {
    private static final Map<String, PoderDC> PODERES_DC = Map.of(
        "Superman", new PoderDC("Poder Kryptoniano", "🌟 Visión láser", 3, false),
        "Batman", new PoderDC("Arsenal Tecnológico", "🦇 Gadgets explosivos", 3, true),
        "Wonder Woman", new PoderDC("Poder Amazónico", "⚔️ Fuerza divina", 2, false),
        "Flash", new PoderDC("Fuerza de Velocidad", "⚡ Velocidad luz", 4, false),
        "Green Lantern", new PoderDC("Anillo de Poder", "💚 Constructos", 3, false)
    );

    private final String superpoder;

    public PersonajeDC(String nombre, int fuerza, int velocidad, int resistencia, String superpoder) {
        super(nombre, fuerza, velocidad, resistencia, "DC Comics");
        this.superpoder = superpoder;
    }

    @Override
    public int poderUnico(Personaje oponente) {
        if (poderEspecialUsado) {
            System.out.println(nombre + " ya usó su superpoder!");
            return 0;
        }

        poderEspecialUsado = true;
        PoderDC poder = PODERES_DC.getOrDefault(nombre, 
            new PoderDC("Superpoder", "Ataque especial", 2, false));
        
        System.out.println(poder.mensaje);
        
        int danio;
        if ("Flash".equals(nombre)) {
            danio = velocidad * poder.multiplicador;
        } else {
            danio = fuerza * poder.multiplicador;
        }
        
        System.out.printf("¡%d puntos de daño devastador!%n", danio);
        oponente.setResistencia(oponente.resistencia - danio);
        
        // Efecto especial de Batman
        if (poder.curativo) {
            recuperarse(10);
            System.out.println("Preparación estratégica: +" + 10 + " resistencia");
        }
        
        return Math.min(danio, oponente.resistencia + danio);
    }

    @Override
    public String getDescripcionPoder() {
        return PODERES_DC.getOrDefault(nombre, 
            new PoderDC("Superpoder", "", 1, false)).nombre;
    }

    private static class PoderDC {
        final String nombre;
        final String mensaje;
        final int multiplicador;
        final boolean curativo;

        PoderDC(String nombre, String mensaje, int multiplicador, boolean curativo) {
            this.nombre = nombre;
            this.mensaje = mensaje;
            this.multiplicador = multiplicador;
            this.curativo = curativo;
        }
    }
}

// Sistema de torneo optimizado
class SistemaTorneo {
    private final List<String> historialGanadores;
    private final int[] puntajesJugadores;
    private static final int MAX_PARTIDAS = 10;

    public SistemaTorneo() {
        this.historialGanadores = new ArrayList<>();
        this.puntajesJugadores = new int[2];
    }

    public void registrarVictoria(String ganador, int numeroJugador, int puntaje) {
        if (puedeJugarMas()) {
            historialGanadores.add(ganador);
            puntajesJugadores[numeroJugador - 1] += puntaje;
        }
    }

    public void mostrarRanking() {
        System.out.println("🏆 === RANKING DEL TORNEO === 🏆");
        System.out.printf("Partidas: %d/%d%n", getPartidasJugadas(), MAX_PARTIDAS);
        System.out.printf("Jugador 1: %d puntos%n", puntajesJugadores[0]);
        System.out.printf("Jugador 2: %d puntos%n", puntajesJugadores[1]);

        if (!historialGanadores.isEmpty()) {
            System.out.println("\nHistorial:");
            for (int i = 0; i < historialGanadores.size(); i++) {
                System.out.printf("Partida %d: %s%n", i + 1, historialGanadores.get(i));
            }
        }

        String lider = determinarLider();
        System.out.println("🎯 Líder: " + lider);
        System.out.println("============================\n");
    }

    private String determinarLider() {
        if (puntajesJugadores[0] > puntajesJugadores[1]) return "Jugador 1";
        if (puntajesJugadores[1] > puntajesJugadores[0]) return "Jugador 2";
        return "Empate";
    }

    public boolean puedeJugarMas() { 
        return historialGanadores.size() < MAX_PARTIDAS; 
    }
    
    public int getPartidasJugadas() { 
        return historialGanadores.size(); 
    }
}

// Estadísticas de batalla optimizadas
class EstadisticasBatalla {
    private int ataques, ataquesCriticos, poderesUsados, recuperaciones;

    public void registrarAtaque() { ataques++; }
    public void registrarAtaqueCritico() { ataquesCriticos++; }
    public void registrarPoderUsado() { poderesUsados++; }
    public void registrarRecuperacion() { recuperaciones++; }

    public void mostrarEstadisticas() {
        System.out.println("📊 === ESTADÍSTICAS BATALLA === 📊");
        System.out.printf("Ataques: %d | Críticos: %d%n", ataques, ataquesCriticos);
        System.out.printf("Poderes: %d | Recuperaciones: %d%n", poderesUsados, recuperaciones);
        System.out.println("==============================\n");
    }
}

// Gestor de entrada optimizado
class GestorEntrada {
    private final Scanner scanner;
    private final Random random;

    public GestorEntrada() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public int seleccionarPersonaje(String jugador, int totalPersonajes, Set<Integer> usados) {
        while (true) {
            try {
                System.out.printf("%s, elige tu luchador (1-%d): ", jugador, totalPersonajes);
                int eleccion = scanner.nextInt();

                if (eleccion < 1 || eleccion > totalPersonajes) {
                    System.out.println("❌ Número inválido");
                    continue;
                }

                if (usados.contains(eleccion)) {
                    System.out.println("❌ Luchador ya elegido");
                    continue;
                }

                return eleccion;
            } catch (InputMismatchException e) {
                System.out.println("❌ Ingresa un número válido");
                scanner.next();
            }
        }
    }

    public TipoAccion seleccionarAccion(Personaje atacante, Personaje defensor) {
        while (true) {
            mostrarOpcionesAccion(atacante, defensor);
            try {
                System.out.print("Elige acción (1-6): ");
                int opcion = scanner.nextInt();
                TipoAccion accion = TipoAccion.fromCodigo(opcion);
                
                if (accion == null) {
                    System.out.println("❌ Opción inválida");
                    continue;
                }

                return accion;
            } catch (InputMismatchException e) {
                System.out.println("❌ Ingresa un número válido");
                scanner.next();
            }
        }
    }

    private void mostrarOpcionesAccion(Personaje atacante, Personaje defensor) {
        System.out.printf("%n🎮 Turno de %s (%s)%n", atacante.getNombre(), atacante.getUniverso());
        System.out.printf("Tu resistencia: %d/%d | Oponente: %d/%d%n", 
                         atacante.getResistencia(), atacante.getResistenciaMaxima(),
                         defensor.getResistencia(), defensor.getResistenciaMaxima());

        for (TipoAccion accion : TipoAccion.values()) {
            String estado = "";
            if (accion == TipoAccion.PODER_UNICO) {
                estado = " (" + atacante.getDescripcionPoder() + 
                        (atacante.isPoderEspecialUsado() ? " - Usado)" : " - Disponible)");
            }
            System.out.println(accion.getDescripcion() + estado);
        }
    }

    public boolean deseaContinuar() {
        while (true) {
            try {
                System.out.print("¿Otra partida? (1=Sí, 2=No): ");
                int respuesta = scanner.nextInt();
                return respuesta == 1;
            } catch (InputMismatchException e) {
                System.out.println("❌ Respuesta inválida");
                scanner.next();
            }
        }
    }

    public boolean esCritico() {
        return random.nextInt(100) < 15; // 15% probabilidad
    }

    public int recuperacionAleatoria() {
        return 20 + random.nextInt(11); // 20-30 puntos
    }

    public boolean turnoAleatorio() {
        return random.nextBoolean();
    }

    public void cerrar() {
        scanner.close();
    }
}

// Motor de juego principal optimizado
class MotorJuego {
    private final GestorEntrada gestorEntrada;
    private final EstadisticasBatalla estadisticas;
    private final SistemaTorneo torneo;
    private final List<Personaje> personajes;

    public MotorJuego() {
        this.gestorEntrada = new GestorEntrada();
        this.estadisticas = new EstadisticasBatalla();
        this.torneo = new SistemaTorneo();
        this.personajes = crearPersonajes();
    }

    private List<Personaje> crearPersonajes() {
        return Arrays.asList(
            // Mortal Kombat
            new PersonajeMK("Scorpion", 35, 25, 120, "Cadenas del Infierno"),
            new PersonajeMK("Sub-Zero", 30, 28, 125, "Maestro del Hielo"),
            new PersonajeMK("Liu Kang", 38, 30, 110, "Dragón de Fuego"),
            new PersonajeMK("Raiden", 32, 27, 130, "Dios del Trueno"),
            new PersonajeMK("Kitana", 28, 35, 105, "Princesa Asesina"),
            
            // DC Comics
            new PersonajeDC("Superman", 45, 30, 150, "Poder Kryptoniano"),
            new PersonajeDC("Batman", 25, 32, 100, "Genio Táctico"),
            new PersonajeDC("Wonder Woman", 40, 28, 135, "Guerrera Amazónica"),
            new PersonajeDC("Flash", 20, 50, 90, "Fuerza de Velocidad"),
            new PersonajeDC("Green Lantern", 35, 25, 115, "Anillo de Poder")
        );
    }

    public void iniciar() {
        mostrarTitulo();
        
        while (torneo.puedeJugarMas()) {
            if (!ejecutarBatalla()) break;
            
            estadisticas.mostrarEstadisticas();
            torneo.mostrarRanking();
            
            if (torneo.puedeJugarMas() && !gestorEntrada.deseaContinuar()) break;
        }
        
        mostrarResultadoFinal();
        gestorEntrada.cerrar();
    }

    private boolean ejecutarBatalla() {
        mostrarPersonajes();
        
        Set<Integer> personajesUsados = new HashSet<>();
        
        int seleccion1 = gestorEntrada.seleccionarPersonaje("🎮 Jugador 1", 
                                                           personajes.size(), personajesUsados);
        personajesUsados.add(seleccion1);
        Personaje jugador1 = personajes.get(seleccion1 - 1);
        jugador1.resetearEstado();
        
        int seleccion2 = gestorEntrada.seleccionarPersonaje("🎮 Jugador 2", 
                                                           personajes.size(), personajesUsados);
        Personaje jugador2 = personajes.get(seleccion2 - 1);
        jugador2.resetearEstado();

        System.out.printf("✅ Jugador 1: %s | Jugador 2: %s%n%n", 
                         jugador1.getNombre(), jugador2.getNombre());

        return ejecutarCombate(jugador1, jugador2);
    }

    private boolean ejecutarCombate(Personaje jugador1, Personaje jugador2) {
        System.out.println("🔥 ¡BATALLA INTERDIMENSIONAL! 🔥\n");
        
        int turno = gestorEntrada.turnoAleatorio() ? 1 : 2;
        System.out.printf("🎲 Empieza Jugador %d (%s)%n", turno, 
                         turno == 1 ? jugador1.getNombre() : jugador2.getNombre());

        int rondas = 0;
        
        while (jugador1.estaVivo() && jugador2.estaVivo()) {
            rondas++;
            System.out.printf("%n🔄 RONDA %d 🔄%n", rondas);

            Personaje atacante = (turno == 1) ? jugador1 : jugador2;
            Personaje defensor = (turno == 1) ? jugador2 : jugador1;

            ResultadoBatalla resultado = ejecutarTurno(atacante, defensor);
            
            if (resultado == ResultadoBatalla.HUIDA) {
                torneo.registrarVictoria(defensor.getNombre(), 
                                       (defensor == jugador1) ? 1 : 2, 50);
                System.out.printf("🏆 %s gana por abandono!%n", defensor.getNombre());
                return true;
            }
            
            if (resultado == ResultadoBatalla.CONTINUA) {
                turno = (turno == 1) ? 2 : 1;
                
                if (rondas % 3 == 0) {
                    System.out.printf("%n📊 Estado (Ronda %d):%n", rondas);
                    System.out.printf("%s: %d HP | %s: %d HP%n", 
                                     jugador1.getNombre(), jugador1.getResistencia(),
                                     jugador2.getNombre(), jugador2.getResistencia());
                }
            }
        }

        // Determinar ganador
        Personaje ganador = jugador1.estaVivo() ? jugador1 : jugador2;
        Personaje perdedor = jugador1.estaVivo() ? jugador2 : jugador1;
        int numeroGanador = (ganador == jugador1) ? 1 : 2;
        
        mostrarFinal(ganador, perdedor);
        
        int puntaje = ganador.getResistencia() + (rondas <= 10 ? 100 : 50);
        torneo.registrarVictoria(ganador.getNombre(), numeroGanador, puntaje);
        
        return true;
    }

    private ResultadoBatalla ejecutarTurno(Personaje atacante, Personaje defensor) {
        TipoAccion accion = gestorEntrada.seleccionarAccion(atacante, defensor);
        
        switch (accion) {
            case ATACAR:
                if (gestorEntrada.esCritico()) {
                    System.out.println("💥 ¡GOLPE CRÍTICO! 💥");
                    atacante.atacarConBonus(defensor, atacante.getFuerza());
                    estadisticas.registrarAtaqueCritico();
                } else {
                    atacante.atacar(defensor);
                    estadisticas.registrarAtaque();
                }
                break;
                
            case ATAQUE_ESPECIAL:
                atacante.ataqueEspecial(defensor);
                estadisticas.registrarAtaque();
                break;
                
            case PODER_UNICO:
                if (atacante.isPoderEspecialUsado()) {
                    System.out.println("❌ Poder ya usado. Pierdes el turno.");
                } else {
                    atacante.poderUnico(defensor);
                    estadisticas.registrarPoderUsado();
                }
                break;
                
            case RECUPERARSE:
                int recuperacion = gestorEntrada.recuperacionAleatoria();
                atacante.recuperarse(recuperacion);
                estadisticas.registrarRecuperacion();
                break;
                
            case VER_ESTADISTICAS:
                atacante.mostrarEstadisticas();
                defensor.mostrarEstadisticas();
                return ejecutarTurno(atacante, defensor); // No consume turno
                
            case HUIR:
                System.out.printf("🏃 %s huye de la batalla!%n", atacante.getNombre());
                return ResultadoBatalla.HUIDA;
        }
        
        return defensor.estaVivo() ? ResultadoBatalla.CONTINUA : ResultadoBatalla.VICTORIA;
    }

    private void mostrarTitulo() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║             MORTAL KOMBAT VS DC COMICS                   ║");
        System.out.println("║                   BATALLA ÉPICA                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
    }

    private void mostrarPersonajes() {
        System.out.println("═══ SELECCIÓN DE LUCHADORES ═══");
        System.out.println("--- MORTAL KOMBAT ---");
        for (int i = 0; i < 5; i++) {
            Personaje p = personajes.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, p.getNombre(), p.getDescripcionPoder());
        }
        System.out.println("\n--- DC COMICS ---");
        for (int i = 5; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, p.getNombre(), p.getDescripcionPoder());
        }
        System.out.println("═══════════════════════════════\n");
    }

    private void mostrarFinal(Personaje ganador, Personaje perdedor) {
        System.out.println("\n🔥🔥🔥 FINAL ÉPICO 🔥🔥🔥");
        System.out.println(perdedor.getNombre() + " está derrotado...");
        System.out.println(ganador.getNombre() + ": \"FINISH HIM!\"");
        System.out.println("💀 FATALITY 💀");
        System.out.printf("🏆 ¡%s GANA LA BATALLA! 🏆%n", ganador.getNombre());
        System.out.println("═══════════════════════════════════════");
    }

    private void mostrarResultadoFinal() {
        System.out.println("\n🎮 ¡Gracias por jugar! 🎮");
        System.out.println("🏆 RESULTADO FINAL 🏆");
        torneo.mostrarRanking();
    }
}

// Clase principal simplificada
public class SimulacionMortalKombat {
    public static void main(String[] args) {
        new MotorJuego().iniciar();
    }
}