// Archivo: JugadorVIP.java
public class JugadorVIP extends Jugador {
    private boolean rerollDisponible = true;
    public JugadorVIP(String nombre, int dineroInicial) {
        super(nombre, dineroInicial);
    }
    @Override
    public int calcularApuesta() { return (int) (getDinero() * 0.30); }
    @Override
    public String obtenerTipoJugador() { return "VIP"; }
    public boolean tieneReroll() { return rerollDisponible; }
    public void usarReroll() { this.rerollDisponible = false; }
}