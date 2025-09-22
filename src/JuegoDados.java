// Archivo: JuegoDados.java
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        }
        System.out.println("\nEl pozo total es de: $" + pozo);

        System.out.println("\n--- Fase de Lanzamientos ---");
        for (Jugador j : jugadores) {
            int res1 = dado1.tirar();
            int res2 = dado2.tirar();
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
            
            resultados.add(new ResultadoRonda(j, suma));
            System.out.println(j.getNombre() + " sacó " + res1 + " + " + res2 + ". Suma total: " + suma);
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