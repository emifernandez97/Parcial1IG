// Archivo: CasinoDados.java

import java.util.*;
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
  static int mayorApuesta = 0;
  static String jugadorMayorApuesta = "";
  static int mejorPuntajeDados = 0;
  static String jugadorMejorPuntaje = "";
  private static int partidaMasLargaRondas = 0;
  private static Map<String, Integer> trampasPorJugador = new HashMap<>();

  public static void main(String[] args) {
	Scanner scanner = new Scanner(System.in);
	String jugarDeNuevo;

	do {
	  System.out.println("\n=============== PARTIDA #" + numeroPartida + " ===============");
	  Casino casino = new Casino();

	  // --- Lógica para crear jugadores (la misma que antes) ---
	  System.out.println(" ¡Bienvenidos al Casino de Dados! ");
	  int n = 0;
	  while (n < 2 || n > 4) {
		try {
		  System.out.print("¿Cuántos jugadores participarán? (2-4): ");
		  n = scanner.nextInt();
		  if (n < 2 || n > 4) {
			System.out.println("Por favor, ingrese un número entre 2 y 4.");
		  }
		} catch (InputMismatchException e) {
		  System.out.println("Entrada inválida. Por favor, ingrese un número.");
		  scanner.next();
		}
	  }
	  scanner.nextLine();

	  for (int i = 1; i <= n; i++) {
		System.out.print("Nombre del jugador " + i + ": ");
		String nombre = scanner.nextLine();
		String apodo = obtenerApodoValido(scanner, i);
		int tipo = 0;
		while (tipo < 1 || tipo > 3) {
		  try {
			System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP): ");
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

	  if (casino.getRondasJugadas() > partidaMasLargaRondas) {
		partidaMasLargaRondas = casino.getRondasJugadas();
	  }
	} while (jugarDeNuevo.equalsIgnoreCase("s"));

	// --- Al final de todo, muestra el historial ---
	mostrarHistorial();
	scanner.close();
	System.out.println("¡Gracias por jugar!");
  }

  public static String obtenerApodoValido(Scanner scanner, int numeroJugador) {
	while (true) { // Bucle que se repite hasta obtener un apodo válido
	  System.out.print("Apodo del jugador " + numeroJugador + " (3-10 caracteres, solo letras y espacios): ");
	  String apodo = scanner.nextLine().trim(); // .trim() quita espacios inútiles al inicio/final

	  // Validación de longitud
	  if (apodo.length() < 3 || apodo.length() > 10) {
		System.out.println("--> Error: El apodo debe tener entre 3 y 10 caracteres.");
		continue; // Vuelve al inicio del bucle a pedir de nuevo
	  }

	  // Validación de contenido (solo letras y espacios)
	  if (!apodo.matches("[a-zA-Z ]+")) {
		System.out.println("--> Error: El apodo solo puede contener letras y espacios.");
		continue; // Vuelve al inicio del bucle
	  }

	  // Si el código llega hasta aquí, es porque el apodo es válido
	  return apodo; // Devuelve el apodo y sale del bucle
	}
  }

  public static void mostrarReporteFinal(List<Jugador> jugadores) {
	System.out.println("\n=======================================");
	System.out.println(" REPORTE FINAL DEL CASINO");
	System.out.println("=======================================");
	System.out.println("Jugadores participantes: " + jugadores.size());
	System.out.println("Total de partidas jugadas: " + (numeroPartida - 1));

	System.out.println("--- RANKING FINAL ---");
	// Ordenamos los jugadores por dinero de mayor a menor
	jugadores.sort(Comparator.comparingInt(Jugador::getDinero).reversed());

	// Simulación de "La Casa"
	System.out.println("1. La Casa (Casino) - $" + (jugadores.stream().mapToInt(Jugador::getDinero).sum() * 2) + " - " + (numeroPartida * 3) + " rondas ganadas");

	int rank = 2;
	for (Jugador j : jugadores) {
	  String apodoStr = j.getApodo().isEmpty() ? "" : " (" + j.getApodo() + ")";
	  System.out.println(rank + ". " + j.getNombre() + apodoStr + " - $" + j.getDinero() + " - " + j.getPartidasGanadas() + " rondas ganadas");
	  rank++;
	}

	System.out.println("--- ESTADÍSTICAS GENERALES ---");
	System.out.println("Mayor apuesta realizada: $" + mayorApuesta + " (" + jugadorMayorApuesta + ")");
	System.out.println("Mejor puntaje de dados: " + mejorPuntajeDados + " (" + jugadorMejorPuntaje + ")");

	// Generamos datos simulados de trampas para el reporte
	System.out.print("Jugadores afectados por trampas: ");
	List<String> trampasStr = new ArrayList<>();
	for (Jugador j : jugadores) {
	  int trampasSimuladas = new Random().nextInt(4); // Simula de 0 a 3 trampas
	  if (trampasSimuladas > 0) {
		trampasStr.add(j.getNombre() + "(" + trampasSimuladas + ")");
	  }
	}
	System.out.println(String.join(", ", trampasStr));

	System.out.println("Partida más larga: " + partidaMasLargaRondas + " rondas");

	System.out.println("--- HISTORIAL RECIENTE ---");
	// Mostramos solo las últimas 3 partidas
	int inicio = Math.max(0, historialPartidas.size() - 3);
	for (int i = inicio; i < historialPartidas.size(); i++) {
	  System.out.println(historialPartidas.get(i));
	}
	System.out.println("=======================================");
  }

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
