// Archivo: JugadorNovato.java
public class JugadorNovato extends Jugador {
    public JugadorNovato(String nombre, int dineroInicial) {
        super(nombre, dineroInicial);
    }
    @Override
    public int calcularApuesta() { return 50; }
    @Override
    public String obtenerTipoJugador() { return "Novato"; }
}