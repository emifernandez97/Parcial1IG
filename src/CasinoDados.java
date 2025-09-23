// Archivo: CasinoDados.java
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CasinoDados {
    // --- ATRIBUTOS PARA EL HISTORIAL ---
    private static List<String> historialPartidas = new ArrayList<>();
    private static final int MAX_HISTORIAL = 5;
    private static int numeroPartida = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String jugarDeNuevo;

        do {
            System.out.println("\n=============== PARTIDA #" + numeroPartida + " ===============");
            Casino casino = new Casino();

            // --- Lógica para crear jugadores (la misma que antes) ---
            System.out.println(" ¡Bienvenidos al Casino de Dados! ");
            int n = 0;
            // ... (código para pedir número de jugadores y sus datos) ...
            // (Este bloque de código es el mismo que ya tenías)
            while (n < 2 || n > 4) {
                try {
                    System.out.print("¿Cuántos jugadores participarán? (2-4): ");
                    n = scanner.nextInt();
                    if (n < 2 || n > 4) System.out.println("Por favor, ingrese un número entre 2 y 4.");
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, ingrese un número.");
                    scanner.next();
                }
            }
            scanner.nextLine();

            for (int i = 1; i <= n; i++) {
                System.out.print("Nombre del jugador " + i + ": ");
                String nombre = scanner.nextLine();
                int tipo = 0;
                while (tipo < 1 || tipo > 3) {
                    try {
                        System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP): ");
                        tipo = scanner.nextInt();
                        if (tipo < 1 || tipo > 3) System.out.println("Por favor, ingrese 1, 2 o 3.");
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada inválida. Por favor, ingrese un número.");
                        scanner.next();
                    }
                }
                scanner.nextLine();
                casino.agregarJugador(casino.crearJugador(nombre, tipo));
            }

            // --- Inicia el juego ---
            casino.jugar(scanner);
            
            // --- Obtiene resultados y arma el historial ---
            Jugador ganador = casino.obtenerGanadorFinal();
            
            StringBuilder detalle = new StringBuilder();
            detalle.append("PARTIDA #").append(numeroPartida);
            
            String nombres = casino.getJugadores().stream()
                                  .map(Jugador::getNombre)
                                  .collect(Collectors.joining(","));
            detalle.append(" - Jugadores: ").append(nombres);
            
            detalle.append(" | Ganador: ").append(ganador != null ? ganador.getNombre() : "Sin ganador");
            
            detalle.append(" | Rondas: ").append(casino.getRondasJugadas());
            
            guardarPartida(detalle.toString());
            numeroPartida++;

            // --- Pregunta si quiere jugar de nuevo ---
            System.out.print("\n¿Quieren jugar otra partida? (s/n): ");
            jugarDeNuevo = scanner.nextLine();

        } while (jugarDeNuevo.equalsIgnoreCase("s"));

        // --- Al final de todo, muestra el historial ---
        mostrarHistorial();
        scanner.close();
        System.out.println("¡Gracias por jugar!");
    }

    /**
     * Guarda el detalle de una partida en el historial.
     * Si el historial supera las 5 entradas, elimina la más antigua.
     */
    public static void guardarPartida(String detalle) {
        historialPartidas.add(detalle);
        if (historialPartidas.size() > MAX_HISTORIAL) {
            historialPartidas.remove(0); // Elimina el primer elemento (el más viejo)
        }
    }

    /**
     * Muestra en pantalla las últimas partidas guardadas.
     */
    public static void mostrarHistorial() {
        System.out.println("\n--- 📜 HISTORIAL DE ÚLTIMAS 5 PARTIDAS 📜 ---");
        if (historialPartidas.isEmpty()) {
            System.out.println("No hay partidas en el historial.");
        } else {
            for (String detalle : historialPartidas) {
                System.out.println(detalle);
            }
        }
        System.out.println("-------------------------------------------");
    }
}