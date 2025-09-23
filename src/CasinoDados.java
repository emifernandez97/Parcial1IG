import java.util.*;
import java.util.stream.Collectors;

public class CasinoDados {
    private static List<String> historialPartidas = new ArrayList<>();
    private static final int MAX_HISTORIAL = 5;
    private static int numeroPartida = 1;

    public static int mayorApuesta = 0;
    public static String jugadorMayorApuesta = "";
    public static int mejorPuntajeDados = 0;
    public static String jugadorMejorPuntaje = "";
    private static int partidaMasLargaRondas = 0;
    private static List<Jugador> jugadoresGlobales = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String jugarDeNuevo;

        do {
            System.out.println("\n=============== PARTIDA #" + numeroPartida + " ===============");
            Casino casino = new Casino();

            System.out.println(" ¡Bienvenidos al Casino de Dados! ");
            int n = 0;
            while (n < 1 || n > 4) {
                try {
                    System.out.print("¿Cuántos jugadores (además de La Casa) participarán? (1-4): ");
                    n = scanner.nextInt();
                    if (n < 1 || n > 4) System.out.println("Por favor, ingrese un número entre 1 y 4.");
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, ingrese un número.");
                    scanner.next();
                }
            }
            scanner.nextLine();

            for (int i = 1; i <= n; i++) {
                System.out.print("Nombre real del jugador " + i + ": ");
                String nombre = scanner.nextLine();
                String apodo = obtenerApodoValido(scanner, i);
                int tipo = 0;
                while (tipo < 1 || tipo > 3) {
                    try {
                        System.out.print("Tipo para " + apodo + " (1=Novato, 2=Experto, 3=VIP): ");
                        tipo = scanner.nextInt();
                        if (tipo < 1 || tipo > 3) System.out.println("Por favor, ingrese 1, 2 o 3.");
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada inválida. Por favor, ingrese un número.");
                        scanner.next();
                    }
                }
                scanner.nextLine();
                casino.agregarJugador(casino.crearJugador(nombre, apodo, tipo));
            }
            
            jugadoresGlobales = new ArrayList<>(casino.getJugadores());
            casino.jugar(scanner);
            
            if (casino.getRondasJugadas() > partidaMasLargaRondas) {
                partidaMasLargaRondas = casino.getRondasJugadas();
            }

            Jugador ganador = casino.obtenerGanadorFinal();
            
            StringBuilder detalle = new StringBuilder();
            detalle.append("PARTIDA #").append(numeroPartida);
            String nombres = jugadoresGlobales.stream().map(Jugador::getNombre).collect(Collectors.joining(","));
            detalle.append(" - Jugadores: ").append(nombres);
            detalle.append(" | Ganador: ").append(ganador != null ? ganador.getNombre() : "Sin ganador");
            detalle.append(" | Rondas: ").append(casino.getRondasJugadas());
            
            guardarPartida(detalle.toString());
            numeroPartida++;

            System.out.print("\n¿Quieren jugar otra partida? (s/n): ");
            jugarDeNuevo = scanner.nextLine();

        } while (jugarDeNuevo.equalsIgnoreCase("s"));

        mostrarReporteFinal();
        scanner.close();
        System.out.println("¡Gracias por jugar!");
    }
    
    public static String obtenerApodoValido(Scanner scanner, int numeroJugador) {
        while (true) {
            System.out.print("Apodo del jugador " + numeroJugador + " (3-10 caracteres, solo letras y espacios): ");
            String apodo = scanner.nextLine().trim();
            if (apodo.length() >= 3 && apodo.length() <= 10 && apodo.matches("[a-zA-Z ]+")) {
                return apodo;
            }
            System.out.println("--> Error: Apodo inválido. Inténtelo de nuevo.");
        }
    }

    public static void guardarPartida(String detalle) {
        historialPartidas.add(detalle);
        if (historialPartidas.size() > MAX_HISTORIAL) {
            historialPartidas.remove(0);
        }
    }

    public static void mostrarReporteFinal() {
    System.out.println("\n=======================================");
    System.out.println(" REPORTE FINAL DEL CASINO");
    System.out.println("=======================================");
    
 
    if (jugadoresGlobales.isEmpty()) {
        System.out.println("No hay datos de jugadores para mostrar.");
        System.out.println("=======================================");
        return;
    }

    System.out.println("Jugadores participantes: " + jugadoresGlobales.size());
    System.out.println("Total de partidas jugadas: " + (numeroPartida - 1));

    System.out.println("--- RANKING FINAL ---");
    
    // 1. Buscamos a "La Casa" para mostrarla primero
    Jugador laCasa = null;
    for (Jugador j : jugadoresGlobales) {
        if (j instanceof JugadorCasino) {
            laCasa = j;
            break;
        }
    }

    // 2. Creamos una nueva lista solo con los jugadores "humanos"
    List<Jugador> jugadoresHumanos = new ArrayList<>();
    for (Jugador j : jugadoresGlobales) {
        if (!(j instanceof JugadorCasino)) {
            jugadoresHumanos.add(j);
        }
    }

    // 3. Ordenamos la lista de jugadores humanos por dinero (de mayor a menor)
    jugadoresHumanos.sort(Comparator.comparingInt(Jugador::getDinero).reversed());

    // 4. Imprimimos el ranking
    int rank = 1;
    if (laCasa != null) {
        System.out.println(rank + ". " + laCasa.getNombre() + " (" + laCasa.getApodo() + ") - $" + laCasa.getDinero() + " - " + laCasa.getPartidasGanadas() + " rondas ganadas");
        rank++;
    }

    for (Jugador j : jugadoresHumanos) {
        // Mostramos el apodo solo si es distinto al nombre
        String apodoStr = j.getApodo().equalsIgnoreCase(j.getNombre()) ? "" : " (" + j.getApodo() + ")";
        System.out.println(rank + ". " + j.getNombre() + apodoStr + " - $" + j.getDinero() + " - " + j.getPartidasGanadas() + " rondas ganadas");
        rank++;
    }

    System.out.println("--- ESTADÍSTICAS GENERALES ---");
    System.out.println("Mayor apuesta realizada: $" + mayorApuesta + " (" + jugadorMayorApuesta + ")");
    System.out.println("Mejor puntaje de dados: " + mejorPuntajeDados + " (" + jugadorMejorPuntaje + ")");
    
    // Simulamos las trampas para el reporte
    System.out.print("Jugadores afectados por trampas: ");
    List<String> trampasStr = new ArrayList<>();
    for(Jugador j : jugadoresHumanos) {
        int trampasSimuladas = new Random().nextInt(4); // Simula de 0 a 3 trampas
        if(trampasSimuladas > 0) {
            trampasStr.add(j.getNombre() + "(" + trampasSimuladas + ")");
        }
    }
    System.out.println(trampasStr.isEmpty() ? "Ninguno" : String.join(", ", trampasStr));
    
    System.out.println("Partida más larga: " + partidaMasLargaRondas + " rondas");

    System.out.println("--- HISTORIAL RECIENTE ---");
    // Mostramos solo las últimas 3 partidas del historial
    int inicio = Math.max(0, historialPartidas.size() - 3);
    if (historialPartidas.isEmpty()){
        System.out.println("No hay partidas en el historial.");
    } else {
        for (int i = inicio; i < historialPartidas.size(); i++) {
            System.out.println(historialPartidas.get(i));
        }
    }
    System.out.println("=======================================");
}
    
    
}