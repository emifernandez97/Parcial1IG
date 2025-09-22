// Archivo: JugadorExperto.java
public class JugadorExperto extends Jugador {
    public JugadorExperto(String nombre, int dineroInicial) {
        super(nombre, dineroInicial);
    }
    @Override
    public int calcularApuesta() { return (int) (getDinero() * 0.20); }
    @Override
    public String obtenerTipoJugador() { return "Experto"; }
}