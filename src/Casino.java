
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Casino {

    private final List<Jugador> jugadores;
    private final JuegoDados juego;
    private int rondasJugadas;

    public Casino() {
        this.jugadores = new ArrayList<>();
        this.juego = new JuegoDados();
        this.rondasJugadas = 0;

        Jugador laCasa = new JugadorCasino("La Casa", "Casino", 99999);
        this.jugadores.add(laCasa);
        System.out.println("-> La Casa se ha unido a la mesa.");
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public int getRondasJugadas() {
        return rondasJugadas;
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public Jugador crearJugador(String nombre, String apodo, int tipo) {
        int dineroInicial = 500;
        return switch (tipo) {
            case 1 ->
                new JugadorNovato(nombre, apodo, dineroInicial);
            case 2 ->
                new JugadorExperto(nombre, apodo, dineroInicial);
            case 3 ->
                new JugadorVIP(nombre, apodo, dineroInicial);
            default -> {
                System.out.println("Tipo inválido. Se asignará como Novato.");
                yield new JugadorNovato(nombre, apodo, dineroInicial);
            }
        };
    }

    // En Casino.java, reemplazar el método jugar()
    public void jugar(Scanner scanner) {
        int rondaActual = 1;
        int rondasMaximas = 3;
        boolean juegoTerminado = false;

        // Bucle 'while' que se ejecuta mientras no se acaben las rondas o se use 'QUIT'
        while (rondaActual <= rondasMaximas && !juegoTerminado) {
            System.out.println("\n<<<<< RONDA " + rondaActual + " de " + rondasMaximas + " >>>>>");
            mostrarEstadoJugadores();

            boolean comandoEjecutado = false;
            do {
                System.out.print("Presioná ENTER para lanzar los dados o ingresá un comando (STATS, QUIT, etc.): ");
                String input = scanner.nextLine();

                if (input.isEmpty()) {
                    // Si el usuario presiona Enter, la ronda avanza
                    comandoEjecutado = false;
                } else {
                    // Si escribe algo, lo procesamos como un comando
                    juegoTerminado = CasinoDados.procesarComando(input, this);
                    comandoEjecutado = true; // Se ejecutó un comando, volvemos a pedir Enter

                    if (juegoTerminado) {
                        break; // Si el comando fue QUIT, salimos del todo
                    }
                }
            } while (comandoEjecutado); // Repetimos si se ejecutó un comando

            if (juegoTerminado) {
                break;
            }

            // --- La lógica de la ronda que ya teníamos ---
            juego.jugarRonda(jugadores, scanner);
            this.rondasJugadas = rondaActual; // Actualizamos el contador de rondas jugadas

            jugadores.removeIf(j -> {
                if (j.getDinero() <= 0 && !(j instanceof JugadorCasino)) {
                    System.out.println("\n" + j.getNombre() + " se ha quedado sin dinero y es eliminado.");
                    return true;
                }
                return false;
            });

            if (jugadores.size() < 2) {
                System.out.println("\n¡El juego termina porque no quedan suficientes jugadores!");
                juegoTerminado = true;
            }

            rondaActual++; // Avanzamos a la siguiente ronda
        }

        System.out.println("\n====== FIN DEL JUEGO ======");
    }

    public Jugador obtenerGanadorFinal() {
        if (jugadores.isEmpty()) {
            return null;
        }

        Jugador ganadorFinal = null;
        int maxDinero = -1;
        for (Jugador j : jugadores) {
            if (j.getDinero() > maxDinero) {
                maxDinero = j.getDinero();
                ganadorFinal = j;
            }
        }
        return ganadorFinal;
    }

    private void mostrarEstadoJugadores() {
        System.out.println("\n--- Estado Actual ---");
        for (Jugador j : jugadores) {
            System.out.println("- " + j.getNombre() + " (" + j.getApodo() + "): $" + j.getDinero());
        }
        System.out.println();
    }
}
