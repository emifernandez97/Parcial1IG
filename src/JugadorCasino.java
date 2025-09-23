import java.util.Random;

public class JugadorCasino extends Jugador {
    private static final double PROBABILIDAD_DADOS_CARGADOS = 0.40; // 40%
    private static final double PROBABILIDAD_CONFUNDIR = 0.30; // 30%

    public JugadorCasino(String nombre, String apodo, int dineroInicial) {
        super(nombre, apodo, dineroInicial);
    }

    @Override
    public int calcularApuesta() {
        return 0; // La Casa no apuesta
    }

    @Override
    public String obtenerTipoJugador() {
        return "La Casa";
    }

    public int lanzarDados(Dado dadoNormal) {
        if (Math.random() < PROBABILIDAD_DADOS_CARGADOS) {
            System.out.println("-> ¡La Casa usa sus dados cargados!");
            return 6;
        } else {
            return dadoNormal.tirar();
        }
    }

    public boolean intentarConfundir(Jugador objetivo) {
        if (Math.random() < PROBABILIDAD_CONFUNDIR) {
            System.out.println("-> ¡La Casa intenta confundir a " + objetivo.getNombre() + "!");
            return true;
        }
        return false;
    }
}