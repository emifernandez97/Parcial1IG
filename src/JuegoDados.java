import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JuegoDados {
    private final Dado dado1;
    private final Dado dado2;

    public JuegoDados() {
        this.dado1 = new Dado();
        this.dado2 = new Dado();
    }

    public void jugarRonda(List<Jugador> jugadores, Scanner scanner) {
        int pozo = 0;
        List<ResultadoRonda> resultados = new ArrayList<>();

        System.out.println("--- Fase de Apuestas ---");
        for (Jugador j : jugadores) {
            int apuesta = j.calcularApuesta();
            if (apuesta > j.getDinero()) {
                apuesta = j.getDinero();
            }
            j.perder(apuesta);
            pozo += apuesta;
            System.out.println(j.getNombre() + " (" + j.obtenerTipoJugador() + ") apuesta $" + apuesta);
            if (apuesta > CasinoDados.mayorApuesta) {
                CasinoDados.mayorApuesta = apuesta;
                CasinoDados.jugadorMayorApuesta = j.getNombre();
            }
        }
        System.out.println("\nEl pozo total es de: $" + pozo);

        System.out.println("\n--- Fase de Lanzamientos ---");
        JugadorCasino laCasa = null;
        Jugador jugadorConfundido = null;

        for (Jugador j : jugadores) {
            if (j instanceof JugadorCasino) {
                laCasa = (JugadorCasino) j;
                break;
            }
        }

        if (laCasa != null) {
            List<Jugador> posiblesVictimas = jugadores.stream()
                    .filter(j -> !(j instanceof JugadorCasino))
                    .collect(Collectors.toList());
            if (!posiblesVictimas.isEmpty()) {
                Jugador victima = posiblesVictimas.get(new Random().nextInt(posiblesVictimas.size()));
                if (laCasa.intentarConfundir(victima)) {
                    jugadorConfundido = victima;
                }
            }
        }

        for (Jugador j : jugadores) {
            int res1, res2;
            if (j instanceof JugadorCasino casa) {
                res1 = casa.lanzarDados(dado1);
                res2 = casa.lanzarDados(dado2);
            } else {
                res1 = dado1.tirar();
                res2 = dado2.tirar();
                if (j == jugadorConfundido) {
                    System.out.println("-> ¡" + j.getNombre() + " está confundido! Sus dados se reducen.");
                    res1 = Math.max(1, res1 - 1);
                    res2 = Math.max(1, res2 - 1);
                }
            }

            int suma = res1 + res2;

            if (j instanceof JugadorVIP vip && vip.tieneReroll()) {
                System.out.println(j.getNombre() + " (VIP) sacó " + res1 + " + " + res2 + " = " + suma);
                System.out.print("¿Deseas usar tu re-roll de dados? (s/n): ");
                if (scanner.nextLine().equalsIgnoreCase("s")) {
                    res1 = dado1.tirar();
                    res2 = dado2.tirar();
                    suma = res1 + res2;
                    vip.usarReroll();
                    System.out.println("¡Nuevo lanzamiento!");
                }
            }
            
            System.out.println(j.getNombre() + " sacó " + res1 + " + " + res2 + ". Suma total: " + suma);
            if (suma > CasinoDados.mejorPuntajeDados) {
                CasinoDados.mejorPuntajeDados = suma;
                CasinoDados.jugadorMejorPuntaje = j.getNombre();
            }
            resultados.add(new ResultadoRonda(j, suma));
        }

        int maxSuma = resultados.stream().mapToInt(ResultadoRonda::getSuma).max().orElse(0);
        List<Jugador> ganadores = resultados.stream()
                .filter(r -> r.getSuma() == maxSuma)
                .map(ResultadoRonda::getJugador)
                .toList();

        System.out.println("\n--- Resultados de la Ronda ---");
        if (ganadores.size() == 1) {
            Jugador ganador = ganadores.get(0);
            ganador.ganar(pozo);
            System.out.println("¡El ganador es " + ganador.getNombre() + " y se lleva $" + pozo + "!");
        } else {
            System.out.println("¡Hubo un empate entre " + ganadores.size() + " jugadores!");
            int premioDividido = pozo / ganadores.size();
            for (Jugador g : ganadores) {
                g.ganar(premioDividido);
                System.out.println(g.getNombre() + " gana $" + premioDividido);
            }
        }
    }
}

class ResultadoRonda {
    private final Jugador jugador;
    private final int suma;
    public ResultadoRonda(Jugador j, int s) { this.jugador = j; this.suma = s; }
    public Jugador getJugador() { return jugador; }
    public int getSuma() { return suma; }
}   