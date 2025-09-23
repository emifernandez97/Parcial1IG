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

    public List<Jugador> getJugadores() { return jugadores; }
    public int getRondasJugadas() { return rondasJugadas; }
    public void agregarJugador(Jugador jugador) { jugadores.add(jugador); }

    public Jugador crearJugador(String nombre, String apodo, int tipo) {
        int dineroInicial = 500;
        return switch (tipo) {
            case 1 -> new JugadorNovato(nombre, apodo, dineroInicial);
            case 2 -> new JugadorExperto(nombre, apodo, dineroInicial);
            case 3 -> new JugadorVIP(nombre, apodo, dineroInicial);
            default -> {
                System.out.println("Tipo inválido. Se asignará como Novato.");
                yield new JugadorNovato(nombre, apodo, dineroInicial);
            }
        };
    }

    public void jugar(Scanner scanner) {
        int rondasMaximas = 3;
        this.rondasJugadas = 0;
        for (int i = 1; i <= rondasMaximas; i++) {
            this.rondasJugadas = i;
            System.out.println("\n<<<<< RONDA " + i + " >>>>>");
            mostrarEstadoJugadores();
            
            juego.jugarRonda(jugadores, scanner);

            jugadores.removeIf(j -> {
                if (j.getDinero() <= 0) {
                    System.out.println("\n" + j.getNombre() + " se ha quedado sin dinero y es eliminado.");
                    return true;
                }
                return false;
            });

            if (jugadores.size() < 2) {
                System.out.println("\n¡El juego termina porque no quedan suficientes jugadores!");
                break;
            }
        }
        System.out.println("\n====== FIN DEL JUEGO ======");
    }
    
    public Jugador obtenerGanadorFinal() {
        if (jugadores.isEmpty()) return null;
        
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