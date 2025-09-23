// Archivo: Casino.java
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
    }
    
    // --- NUEVO MÉTODO GETTER ---
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    // --- NUEVO MÉTODO GETTER ---
    public int getRondasJugadas() {
        return rondasJugadas;
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }
    
    public Jugador crearJugador(String nombre, int tipo) {
        int dineroInicial = 500;
        return switch (tipo) {
            case 1 -> new JugadorNovato(nombre, dineroInicial);
            case 2 -> new JugadorExperto(nombre, dineroInicial);
            case 3 -> new JugadorVIP(nombre, dineroInicial);
            default -> {
                System.out.println("Tipo inválido. Se asignará como Novato.");
                yield new JugadorNovato(nombre, dineroInicial);
            }
        };
    }

    public void jugar(Scanner scanner) {
        
        int rondasMaximas = 5;
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
        //mostrarEstadoFinal()
    }
    
    ///ACtividadParcial
    /**
     * MÉTODO MODIFICADO: Ahora busca y DEVUELVE al ganador final.
     * @return El objeto Jugador que ganó, o null si no hay ganador.
     */
    public Jugador obtenerGanadorFinal() {
        mostrarEstadoFinal(); // Imprime los detalles como antes
        
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
        
        if(ganadorFinal != null){
             System.out.println("\n¡El campeón del casino es " + ganadorFinal.getNombre() + "!");
        }
       
        return ganadorFinal;
    }
    //FinActividadParcial
    
    
    private void mostrarEstadoJugadores() {
        System.out.println("\n--- Estado Actual ---");
        for (Jugador j : jugadores) {
            System.out.println("- " + j.getNombre() + " (" + j.obtenerTipoJugador() + "): $" + j.getDinero());
        }
        System.out.println();
    }
    
    private void mostrarEstadoFinal() {
        System.out.println("--- Resultados Finales ---");
        Jugador ganadorFinal = null;
        int maxDinero = -1;

        if (jugadores.isEmpty()) {
            System.out.println("¡Todos los jugadores han sido eliminados!");
            return;
        }

        for (Jugador j : jugadores) {
            System.out.println("- " + j.getNombre() + " terminó con $" + j.getDinero() + " y " + j.getPartidasGanadas() + " rondas ganadas.");
            if (j.getDinero() > maxDinero) {
                maxDinero = j.getDinero();
                ganadorFinal = j;
            }
        }
        
        System.out.println("\n¡El campeón del casino es " + ganadorFinal.getNombre() + "!");
    }
}