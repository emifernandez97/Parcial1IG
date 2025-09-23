
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
    private static List<String> registroTrampas = new ArrayList<>();

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
                    if (n < 1 || n > 4) {
                        System.out.println("Por favor, ingrese un número entre 1 y 4.");
                    }
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
                        if (tipo < 1 || tipo > 3) {
                            System.out.println("Por favor, ingrese 1, 2 o 3.");
                        }
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

    public static void registrarTrampa(String detalle) {
        registroTrampas.add(detalle);
    }

    public static void mostrarRegistroTrampas() {
        System.out.println("--- 📜 REGISTRO DE TRAMPAS DE LA CASA 📜 ---");
        if (registroTrampas.isEmpty()) {
            System.out.println("La Casa se ha portado bien (hasta ahora)...");
        } else {
            for (String trampa : registroTrampas) {
                System.out.println("- " + trampa);
            }
        }
        System.out.println("-------------------------------------------");
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

    public static void mostrarRankingActual(List<Jugador> jugadores) {
        System.out.println("--- RANKING ACTUAL (POR DINERO) ---");
        // Creamos una copia para no modificar la lista original del juego
        List<Jugador> copiaJugadores = new ArrayList<>(jugadores);
        copiaJugadores.sort(Comparator.comparingInt(Jugador::getDinero).reversed());

        int rank = 1;
        for (Jugador j : copiaJugadores) {
            String apodoStr = j.getApodo().equalsIgnoreCase(j.getNombre()) ? "" : " (" + j.getApodo() + ")";
            System.out.println(rank + ". " + j.getNombre() + apodoStr + " - $" + j.getDinero());
            rank++;
        }
        System.out.println("-----------------------------------");
    }

    /**
     * Procesa un comando ingresado por el usuario.
     *
     * @return 'true' si el comando es 'QUIT', 'false' en otro caso.
     */
    public static boolean procesarComando(String input, Casino casino) {
        // separamos el comando de sus posibles argumentos (para "SAVE [nombre]")
        String[] partes = input.trim().toUpperCase().split(" ", 2);
        String comando = partes[0];

        switch (comando) {
            case "STATS":
                // Lógica para mostrar estadísticas actuales (podemos crear un método para esto)
                System.out.println("--- ESTADÍSTICAS ACTUALES ---");
                System.out.println("Mayor apuesta: $" + mayorApuesta + " (" + jugadorMayorApuesta + ")");
                System.out.println("Mejor puntaje de dados: " + mejorPuntajeDados + " (" + jugadorMejorPuntaje + ")");
                System.out.println("-----------------------------");
                break;

            case "HISTORY":
                mostrarHistorial(); // Reutilizamos el método que ya teníamos
                break;

            case "RANKING":
                mostrarRankingActual(casino.getJugadores());
                break;

            case "TRAMPAS":
                mostrarRegistroTrampas();
                break;

            case "SAVE":
                // Versión simple: solo mostramos un mensaje de guardado.
                // Una versión real guardaría el estado del juego en un archivo.
                if (partes.length > 1) {
                    System.out.println(">>> Partida guardada con el nombre: " + partes[1] + " (simulación)");
                } else {
                    System.out.println(">>> Para guardar, usá el formato: SAVE [nombre]");
                }
                break;

            case "QUIT":
                System.out.println(">>> Saliendo de la partida...");
                return true; // Devuelve 'true' para indicar que el juego debe terminar

            default:
                // Si no es un comando, el juego debe continuar.
                // Devolvemos 'false' para que el bucle de juego no se rompa.
                return false;
        }

        // Si el comando fue uno de los que no terminan el juego, devolvemos 'false'
        // pero indicamos que el turno no debe avanzar. Lo manejaremos en la clase Casino.
        // Por ahora, simplemente no avanzamos la ronda.
        return false; // Por defecto, el juego no termina
    }

    /**
     * Muestra en pantalla las últimas partidas guardadas en el historial.
     */
    public static void mostrarHistorial() {
        System.out.println("\n--- 📜 HISTORIAL RECIENTE 📜 ---");
        // Mostramos solo las últimas 3 partidas del historial para que coincida con el reporte
        int inicio = Math.max(0, historialPartidas.size() - 3);

        if (historialPartidas.isEmpty()) {
            System.out.println("No hay partidas en el historial.");
        } else {
            for (int i = inicio; i < historialPartidas.size(); i++) {
                System.out.println(historialPartidas.get(i));
            }
        }
        System.out.println("---------------------------------");
    }

    public static void mostrarReporteFinal() {
        // 1. Creamos un StringBuilder para construir todo nuestro reporte.
        StringBuilder reporte = new StringBuilder();

        // 2. Usamos .append() para agregar cada línea. Agregamos "\n" al final para el salto de línea.
        reporte.append("\n=======================================\n");
        reporte.append(" REPORTE FINAL DEL CASINO\n");
        reporte.append("=======================================\n");

        if (jugadoresGlobales.isEmpty()) {
            reporte.append("No hay datos de jugadores para mostrar.\n");
            reporte.append("=======================================\n");
            System.out.println(reporte.toString()); // Imprimimos y salimos
            return;
        }

        reporte.append("Jugadores participantes: ").append(jugadoresGlobales.size()).append("\n");
        reporte.append("Total de partidas jugadas: ").append(numeroPartida - 1).append("\n");

        reporte.append("--- RANKING FINAL ---\n");

        Jugador laCasa = null;
        List<Jugador> jugadoresHumanos = new ArrayList<>();
        for (Jugador j : jugadoresGlobales) {
            if (j instanceof JugadorCasino) {
                laCasa = j;
            } else {
                jugadoresHumanos.add(j);
            }
        }

        jugadoresHumanos.sort(Comparator.comparingInt(Jugador::getDinero).reversed());

        int rank = 1;
        if (laCasa != null) {
            reporte.append(rank).append(". ").append(laCasa.getNombre())
                    .append(" (").append(laCasa.getApodo()).append(") - $")
                    .append(laCasa.getDinero()).append(" - ")
                    .append(laCasa.getPartidasGanadas()).append(" rondas ganadas\n");
            rank++;
        }

        for (Jugador j : jugadoresHumanos) {
            String apodoStr = j.getApodo().equalsIgnoreCase(j.getNombre()) ? "" : " (" + j.getApodo() + ")";
            reporte.append(rank).append(". ").append(j.getNombre()).append(apodoStr)
                    .append(" - $").append(j.getDinero()).append(" - ")
                    .append(j.getPartidasGanadas()).append(" rondas ganadas\n");
            rank++;
        }

        reporte.append("--- ESTADÍSTICAS GENERALES ---\n");
        reporte.append("Mayor apuesta realizada: $").append(mayorApuesta)
                .append(" (").append(jugadorMayorApuesta).append(")\n");
        reporte.append("Mejor puntaje de dados: ").append(mejorPuntajeDados)
                .append(" (").append(jugadorMejorPuntaje).append(")\n");

        reporte.append("Jugadores afectados por trampas: ");
        List<String> trampasStr = new ArrayList<>();
        for (Jugador j : jugadoresHumanos) {
            int trampasSimuladas = new Random().nextInt(4);
            if (trampasSimuladas > 0) {
                trampasStr.add(j.getNombre() + "(" + trampasSimuladas + ")");
            }
        }
        reporte.append(trampasStr.isEmpty() ? "Ninguno" : String.join(", ", trampasStr)).append("\n");

        reporte.append("Partida más larga: ").append(partidaMasLargaRondas).append(" rondas\n");

        /*
        reporte.append("--- HISTORIAL RECIENTE ---\n");
        int inicio = Math.max(0, historialPartidas.size() - 3);
        if (historialPartidas.isEmpty()) {
            reporte.append("No hay partidas en el historial.\n");
        } else {
            for (int i = inicio; i < historialPartidas.size(); i++) {
                reporte.append(historialPartidas.get(i)).append("\n");
            }
        }
        */
        mostrarHistorial(); // Llamamos al método especializado
        reporte.append("=======================================\n");

        // 3. Imprimimos el contenido completo del StringBuilder DE UNA SOLA VEZ.
        System.out.println(reporte.toString());
    }

}
